package com.zzaug.api.domain.member.usecase;

import static com.zzaug.api.domain.member.model.auth.EmailAuthResult.NOT_MATCH_CODE;
import static com.zzaug.api.domain.member.model.auth.EmailAuthResult.SUCCESS;

import com.zzaug.api.domain.member.dao.auth.EmailAuthDao;
import com.zzaug.api.domain.member.data.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.member.data.entity.auth.EmailData;
import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.dto.SuccessCheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.exception.argument.NotMatchEmailAuthException;
import com.zzaug.api.domain.member.exception.strategy.IllegalMemberRequestStrategyException;
import com.zzaug.api.domain.member.exception.strategy.IllegalNonceRequestStrategyException;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthToken;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthTokenGenerator;
import com.zzaug.api.domain.member.model.auth.EmailAuthResult;
import com.zzaug.api.domain.member.model.auth.EmailAuthSource;
import com.zzaug.api.domain.member.model.auth.TryCount;
import com.zzaug.api.domain.member.model.member.MemberSource;
import com.zzaug.api.domain.member.service.history.GetEmailAuthCheckTryCountService;
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

	private final EmailAuthDao emailAuthDao;

	private final GetMemberSourceQuery getMemberSourceQuery;

	private final SaveEmailAuthHistoryCommand saveEmailAuthHistoryCommand;

	private final GetEmailAuthCheckTryCountService getEmailAuthCheckTryCountService;

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

		// 이메일 인증 확인 요청한 멤버와 해당 API를 요청한 멤버가 일치하는지 확인
		if (!emailAuth.isMemberId(memberSource.getId())) {
			throw new IllegalMemberRequestStrategyException();
		}
		// 이메일 인증 요청시 발급한 nonce와 요청한 nonce가 일치하는지 확인
		if (!emailAuth.isNonce(nonce)) {
			throw new IllegalNonceRequestStrategyException();
		}

		// 이메일 인증을 시도한 횟수를 조회
		TryCount tryCount = getEmailAuthCheckTryCountService.execute(memberId, emailAuthId);
		if (tryCount.isOver()) {
			return CheckEmailAuthUseCaseResponse.builder()
					.authentication(false)
					.tryCount((long) tryCount.getTryCount())
					.build();
		}

		// 이메일 인증 요청시 발급한 code와 요청한 code가 일치하는지 확인
		if (!emailAuth.isCode(code)) {
			tryCount = calculateTryCount(NOT_MATCH_CODE, tryCount);
			EmailAuthHistoryEntity emailAuthHistoryEntity =
					saveEmailAuthHistoryCommand.execute(
							tryCount, memberId, emailAuthId, NOT_MATCH_CODE.getReason());

			return CheckEmailAuthUseCaseResponse.builder()
					.authentication(false)
					.tryCount(emailAuthHistoryEntity.getTryCount())
					.build();
		}

		tryCount = calculateTryCount(SUCCESS, tryCount);
		EmailAuthHistoryEntity emailAuthHistoryEntity =
				saveEmailAuthHistoryCommand.execute(tryCount, memberId, emailAuthId, SUCCESS.getReason());

		// 멤버의 정보가 수정되었기에 새로운 토큰을 발급
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
			throw new NotMatchEmailAuthException();
		}
		return EmailAuthSourceConverter.from(emailAuthSource.get());
	}

	private TryCount calculateTryCount(EmailAuthResult result, TryCount tryCount) {
		if (!result.isSuccess()) {
			tryCount.plus();
		}
		return tryCount;
	}
}
