package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.EmailAuthUseCaseRequest;
import com.zzaug.api.domain.dto.member.EmailAuthUseCaseResponse;
import com.zzaug.api.domain.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.entity.auth.EmailData;
import com.zzaug.api.domain.entity.member.ContactType;
import com.zzaug.api.domain.event.member.MemberEmailAuthMessage;
import com.zzaug.api.domain.external.dao.auth.EmailAuthDao;
import com.zzaug.api.domain.external.dao.member.ExternalContactDao;
import com.zzaug.api.domain.redis.email.EmailAuthSession;
import com.zzaug.api.domain.support.usecase.RandomAuthCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAuthUseCase {

	private final ExternalContactDao externalContactDao;
	private final EmailAuthDao emailAuthDao;
	private final RandomAuthCodeGenerator randomAuthCodeGenerator;

	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public EmailAuthUseCaseResponse execute(EmailAuthUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final String sessionId = request.getSessionId();
		final EmailData email = EmailData.builder().email(request.getEmail()).build();
		final String nonce = request.getNonce();
		final String authCode = randomAuthCodeGenerator.generate(7);
		log.debug("AuthCode is generated : {}", authCode);

		log.debug("Check duplicate email. email: {}", email.getEmail());
		boolean isEmailExist =
				externalContactDao.existsByContactTypeAndSourceAndDeletedFalse(
						ContactType.EMAIL, email.getEmail());
		if (isEmailExist) {
			return EmailAuthUseCaseResponse.builder().duplication(true).build();
		}
		// todo 너무 많은 이메일 인증 요청을 보내는 것은 아닌지 확인한다.

		log.debug(
				"Save email auth entity and session. memberId: {}, email: {}, sessionId: {}",
				memberId,
				email.getEmail(),
				sessionId);
		save(memberId, email, nonce, authCode, sessionId);

		publishEvent(memberId, email, authCode);

		return EmailAuthUseCaseResponse.builder().duplication(false).build();
	}

	private void save(
			Long memberId, EmailData email, String nonce, String authCode, String sessionId) {
		EmailAuthEntity emailAuthEntity = saveEmailAuthEntity(memberId, email, nonce, authCode);
		saveEmailSession(memberId, sessionId, emailAuthEntity.getId());
	}

	private EmailAuthEntity saveEmailAuthEntity(
			Long memberId, EmailData email, String nonce, String authCode) {
		EmailAuthEntity emailAuthSource =
				EmailAuthEntity.builder()
						.memberId(memberId)
						.email(email)
						.nonce(nonce)
						.code(authCode)
						.build();
		return emailAuthDao.saveEmailAuth(emailAuthSource);
	}

	private void saveEmailSession(Long memberId, String sessionId, Long emailAuthId) {
		EmailAuthSession emailAuthSession =
				EmailAuthSession.builder()
						.memberId(memberId)
						.emailAuthId(emailAuthId)
						.sessionId(sessionId)
						.build();
		emailAuthDao.saveEmailAuthSession(emailAuthSession);
	}

	private void publishEvent(Long memberId, EmailData email, String authCode) {
		log.debug(
				"Publish email auth event. memberId: {}, email: {}, code: {}",
				memberId,
				email.getEmail(),
				authCode);
		applicationEventPublisher.publishEvent(
				MemberEmailAuthMessage.builder()
						.memberId(memberId)
						.email(email.getEmail())
						.code(authCode)
						.build());
	}
}
