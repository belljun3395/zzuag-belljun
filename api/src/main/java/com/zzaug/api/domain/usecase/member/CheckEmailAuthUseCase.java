package com.zzaug.api.domain.usecase.member;

import static com.zzaug.api.domain.model.auth.EmailAuthResult.NOT_MATCH_CODE;
import static com.zzaug.api.domain.model.auth.EmailAuthResult.SUCCESS;

import com.zzaug.api.domain.dto.member.CheckEmailAuthUseCaseRequest;
import com.zzaug.api.domain.dto.member.CheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.dto.member.SuccessCheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.entity.auth.EmailData;
import com.zzaug.api.domain.entity.log.EmailAuthLogEntity;
import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.ContactType;
import com.zzaug.api.domain.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.event.member.AddEmailEvent;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.InvalidRequestException;
import com.zzaug.api.domain.exception.OverMaxTryCountException;
import com.zzaug.api.domain.exception.SourceNotFoundException;
import com.zzaug.api.domain.external.dao.auth.EmailAuthDao;
import com.zzaug.api.domain.external.dao.log.EmailAuthLogDao;
import com.zzaug.api.domain.external.dao.member.AuthenticationDao;
import com.zzaug.api.domain.external.dao.member.ExternalContactDao;
import com.zzaug.api.domain.external.security.AuthTokenValidator;
import com.zzaug.api.domain.external.security.auth.BlackTokenAuthsCommand;
import com.zzaug.api.domain.external.security.auth.ReplaceTokenCacheService;
import com.zzaug.api.domain.external.service.auth.EmailAuthLogService;
import com.zzaug.api.domain.external.service.member.MemberSourceQuery;
import com.zzaug.api.domain.model.auth.EmailAuthSource;
import com.zzaug.api.domain.model.auth.TryCountElement;
import com.zzaug.api.domain.model.member.MemberAuthentication;
import com.zzaug.api.domain.model.member.MemberSource;
import com.zzaug.api.domain.redis.email.EmailAuthSession;
import com.zzaug.api.domain.support.entity.EmailAuthSourceConverter;
import com.zzaug.api.domain.support.entity.MemberAuthenticationConverter;
import com.zzaug.api.domain.support.security.RoleUserAuthToken;
import com.zzaug.api.domain.support.security.RoleUserAuthTokenGenerator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckEmailAuthUseCase {

	private static final int MAX_TRY_COUNT = 3;

	private final AuthenticationDao authenticationDao;
	private final ExternalContactDao externalContactDao;
	private final EmailAuthDao emailAuthDao;
	private final EmailAuthLogDao emailAuthLogDao;

	private final MemberSourceQuery memberSourceQuery;
	private final EmailAuthLogService emailAuthLogService;

	private final ApplicationEventPublisher applicationEventPublisher;

	// security
	private final AuthTokenValidator authTokenValidator;
	private final BlackTokenAuthsCommand blackTokenAuthCommand;
	private final ReplaceTokenCacheService replaceTokenCacheService;

	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public CheckEmailAuthUseCaseResponse execute(CheckEmailAuthUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final String code = request.getCode();
		final EmailData email = EmailData.builder().email(request.getEmail()).build();
		final String nonce = request.getNonce();
		final String sessionId = request.getSessionId();
		final String accessToken = request.getAccessToken();
		final String refreshToken = request.getRefreshToken();

		authTokenValidator.execute(refreshToken, accessToken, memberId);

		log.debug("Check email auth session. sessionId: {}", sessionId);
		Optional<EmailAuthSession> emailAuthSessionSource = emailAuthDao.findBySessionId(sessionId);
		if (emailAuthSessionSource.isEmpty()) {
			throw new SourceNotFoundException(DBSource.EMAIL_AUTH_SESSION, "SessionId", sessionId);
		}

		MemberSource memberSource = memberSourceQuery.execute(memberId);

		log.debug("Get email auth source. memberId: {}, email: {}, nonce: {}", memberId, email, nonce);
		EmailAuthSource emailAuth = getEmailAuth(memberSource, email, nonce);
		final Long emailAuthId = emailAuth.getId();

		// 이메일 인증 요청한 멤버와 요청한 멤버가 일치하는지 확인
		if (!emailAuth.isMemberId(memberSource.getId())) {
			throw new InvalidRequestException("request member id is not matched", null);
		}
		// 이메일 인증 요청시 발급한 nonce와 요청한 nonce가 일치하는지 확인
		if (!emailAuth.isNonce(nonce)) {
			throw new InvalidRequestException("request nonce is not matched", "nonce");
		}

		log.debug("Get try count. memberId: {}, emailAuthId: {}", memberId, emailAuthId);
		// 이메일 인증을 시도한 횟수를 조회
		TryCountElement tryCount = null;
		try {
			tryCount = getTryCount(memberId, emailAuthId);
		} catch (OverMaxTryCountException e) {
			log.debug("Delete email auth session because tryCount is over max. sessionId: {}", sessionId);
			emailAuthDao.deleteBySessionId(sessionId);
			return CheckEmailAuthUseCaseResponse.builder()
					.authentication(false)
					.tryCount(Long.valueOf(MAX_TRY_COUNT))
					.build();
		}
		assert tryCount != null;

		// 이메일 인증 요청시 발급한 code와 요청한 code가 일치하는지 확인
		if (!emailAuth.isCode(code)) {
			log.debug("Not match code. memberId: {}, emailAuthId: {}", memberId, emailAuthId);
			tryCount = emailAuthLogService.calculateTryCount(NOT_MATCH_CODE, tryCount);
			EmailAuthLogEntity emailAuthLogEntity =
					emailAuthLogService.saveLog(tryCount, memberId, emailAuthId, NOT_MATCH_CODE.getReason());
			log.debug(
					"Save Fail email auth log. memberId: {}, emailAuthId: {}, emailAuthLogId: {}",
					memberId,
					emailAuthId,
					emailAuthLogEntity.getId());
			return CheckEmailAuthUseCaseResponse.builder()
					.authentication(false)
					.tryCount(emailAuthLogEntity.getTryCount())
					.build();
		}
		// 일치하는 이메일을 외부 연락처로 저장
		ExternalContactEntity externalContactEntity =
				ExternalContactEntity.builder()
						.memberId(memberSource.getId())
						.contactType(ContactType.EMAIL)
						.source(email.getEmail())
						.build();
		Long savedExternalContactId = externalContactDao.saveContact(externalContactEntity).getId();
		log.debug(
				"Save external contact. memberId: {}, contactId: {}", memberId, savedExternalContactId);
		tryCount = emailAuthLogService.calculateTryCount(SUCCESS, tryCount);
		EmailAuthLogEntity emailAuthLogEntity =
				emailAuthLogService.saveLog(tryCount, memberId, emailAuthId, SUCCESS.getReason());
		log.debug(
				"Save Success email auth log. memberId: {}, emailAuthId: {}, emailAuthLogId: {}",
				memberId,
				emailAuthId,
				emailAuthLogEntity.getId());
		log.debug("Delete email auth session. sessionId: {}", sessionId);
		emailAuthDao.deleteBySessionId(sessionId);

		log.debug("Get authentication. memberId: {}", memberId);
		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByMemberIdAndDeletedFalse(memberId);
		if (authenticationSource.isEmpty()) {
			throw new SourceNotFoundException(DBSource.AUTHENTICATION, "MemberId", memberId);
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		RoleUserAuthToken authToken = roleUserAuthTokenGenerator.generateToken(memberSource.getId());

		blackTokenAuthCommand.execute(accessToken, refreshToken);
		replaceTokenCacheService.execute(
				accessToken, authToken.getAccessToken(), memberAuthentication.getMemberId());

		publishEvent(memberId, memberAuthentication, email);

		return SuccessCheckEmailAuthUseCaseResponse.builder()
				.authentication(true)
				.tryCount(emailAuthLogEntity.getTryCount())
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}

	private EmailAuthSource getEmailAuth(MemberSource memberSource, EmailData email, String nonce) {
		Optional<EmailAuthEntity> emailAuthSource =
				emailAuthDao.findByMemberIdAndEmailAndNonceAndDeletedFalse(
						memberSource.getId(), email, nonce);
		if (emailAuthSource.isEmpty()) {
			throw new SourceNotFoundException(DBSource.EMAIL_AUTH, "MemberId", memberSource.getId());
		}
		return EmailAuthSourceConverter.from(emailAuthSource.get());
	}

	private TryCountElement getTryCount(Long memberId, Long emailAuthId) {
		// 이메일 인증을 실패한 이력이 있는지 조회
		Optional<EmailAuthLogEntity> emailAuthLogSource =
				emailAuthLogDao.findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
						memberId, emailAuthId, SUCCESS.getReason());
		int tryCount;
		if (emailAuthLogSource.isEmpty()) {
			// 이메일 인증을 실패한 이력이 없는 경우 tryCount를 0으로 초기화
			tryCount = 0;
			return TryCountElement.builder().tryCount(tryCount).build();
		} else {
			// 이메일 인증을 실패한 이력이 있는 경우 tryCount를 조회하여 초기화
			tryCount = Math.toIntExact(emailAuthLogSource.get().getTryCount());
			if (tryCount >= MAX_TRY_COUNT) {
				throw new OverMaxTryCountException("request try count is over");
			}
			return TryCountElement.builder()
					.tryCount(tryCount)
					.emailAuthLogId(emailAuthLogSource.get().getId())
					.build();
		}
	}

	private void publishEvent(Long memberId, MemberAuthentication authentication, EmailData email) {
		applicationEventPublisher.publishEvent(
				AddEmailEvent.builder()
						.memberId(memberId)
						.memberCertification(authentication.getCertification())
						.memberEmail(email.getEmail())
						.build());
	}
}
