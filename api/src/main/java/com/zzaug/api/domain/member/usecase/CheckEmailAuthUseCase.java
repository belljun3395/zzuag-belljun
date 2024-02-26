package com.zzaug.api.domain.member.usecase;

import static com.zzaug.api.domain.member.model.auth.EmailAuthResult.NOT_MATCH_CODE;
import static com.zzaug.api.domain.member.model.auth.EmailAuthResult.SUCCESS;

import com.zzaug.api.domain.member.dao.auth.EmailAuthDao;
import com.zzaug.api.domain.member.dao.history.EmailAutHistoryDao;
import com.zzaug.api.domain.member.data.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.member.data.entity.auth.EmailData;
import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.dto.SuccessCheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthToken;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthTokenGenerator;
import com.zzaug.api.domain.member.model.auth.EmailAuthSource;
import com.zzaug.api.domain.member.model.auth.TryCountElement;
import com.zzaug.api.domain.member.model.member.MemberSource;
import com.zzaug.api.domain.member.service.history.CalculateTryCountService;
import com.zzaug.api.domain.member.service.history.SaveEmailAuthHistoryCommand;
import com.zzaug.api.domain.member.service.member.GetMemberSourceQuery;
import com.zzaug.api.domain.member.util.entity.EmailAuthSourceConverter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckEmailAuthUseCase {

	private static final int MAX_TRY_COUNT = 3;

	private final EmailAuthDao emailAuthDao;
	private final EmailAutHistoryDao emailAutHistoryDao;

	private final GetMemberSourceQuery getMemberSourceQuery;

	private final CalculateTryCountService calculateTryCountService;
	private final SaveEmailAuthHistoryCommand saveEmailAuthHistoryCommand;

	// security
	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public CheckEmailAuthUseCaseResponse execute(CheckEmailAuthUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final String code = request.getCode();
		final EmailData email = EmailData.builder().email(request.getEmail()).build();
		final String nonce = request.getNonce();

		MemberSource memberSource = getMemberSourceQuery.execute(memberId);

		EmailAuthSource emailAuth = getEmailAuth(memberSource, email, nonce);
		final Long emailAuthId = emailAuth.getId();

		// 이메일 인증 요청한 멤버와 요청한 멤버가 일치하는지 확인
		if (!emailAuth.isMemberId(memberSource.getId())) {
			throw new IllegalArgumentException();
		}
		// 이메일 인증 요청시 발급한 nonce와 요청한 nonce가 일치하는지 확인
		if (!emailAuth.isNonce(nonce)) {
			throw new IllegalArgumentException();
		}

		// 이메일 인증을 시도한 횟수를 조회
		TryCountElement tryCount = null;
		try {
			tryCount = getTryCount(memberId, emailAuthId);
		} catch (IllegalStateException e) {
			return CheckEmailAuthUseCaseResponse.builder()
					.authentication(false)
					.tryCount(Long.valueOf(MAX_TRY_COUNT))
					.build();
		}
		assert tryCount != null;

		// 이메일 인증 요청시 발급한 code와 요청한 code가 일치하는지 확인
		if (!emailAuth.isCode(code)) {
			tryCount = calculateTryCountService.execute(NOT_MATCH_CODE, tryCount);
			EmailAuthHistoryEntity emailAuthHistoryEntity =
					saveEmailAuthHistoryCommand.execute(
							tryCount, memberId, emailAuthId, NOT_MATCH_CODE.getReason());

			return CheckEmailAuthUseCaseResponse.builder()
					.authentication(false)
					.tryCount(emailAuthHistoryEntity.getTryCount())
					.build();
		}

		tryCount = calculateTryCountService.execute(SUCCESS, tryCount);
		EmailAuthHistoryEntity emailAuthHistoryEntity =
				saveEmailAuthHistoryCommand.execute(tryCount, memberId, emailAuthId, SUCCESS.getReason());

		RoleUserAuthToken authToken = roleUserAuthTokenGenerator.generateToken(memberSource.getId());

		return SuccessCheckEmailAuthUseCaseResponse.builder()
				.authentication(true)
				.tryCount(emailAuthHistoryEntity.getTryCount())
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}

	private EmailAuthSource getEmailAuth(MemberSource memberSource, EmailData email, String nonce) {
		Optional<EmailAuthEntity> emailAuthSource =
				emailAuthDao.findByMemberIdAndEmailAndNonceAndDeletedFalse(
						memberSource.getId(), email, nonce);
		if (emailAuthSource.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return EmailAuthSourceConverter.from(emailAuthSource.get());
	}

	private TryCountElement getTryCount(Long memberId, Long emailAuthId) {
		// 이메일 인증을 실패한 이력이 있는지 조회
		Optional<EmailAuthHistoryEntity> emailAuthLogSource =
				emailAutHistoryDao.findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
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
				throw new IllegalStateException();
			}
			return TryCountElement.builder()
					.tryCount(tryCount)
					.emailAuthLogId(emailAuthLogSource.get().getId())
					.build();
		}
	}
}
