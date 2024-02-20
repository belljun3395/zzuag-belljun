package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.LogOutUseCaseRequest;
import com.zzaug.api.domain.entity.log.LoginLogEntity;
import com.zzaug.api.domain.entity.log.LoginStatus;
import com.zzaug.api.domain.event.member.LogOutEvent;
import com.zzaug.api.domain.external.dao.log.LoginLogDao;
import com.zzaug.api.domain.external.security.AuthTokenValidator;
import com.zzaug.api.domain.external.security.auth.BlackTokenAuthsCommand;
import com.zzaug.api.domain.external.security.auth.EnrollBlackTokenCacheCommand;
import com.zzaug.api.domain.external.security.auth.TokenCacheEvictCommand;
import com.zzaug.api.domain.external.service.log.LoginLogCommand;
import com.zzaug.api.domain.model.log.LoginLog;
import com.zzaug.api.domain.support.entity.LoginLogConverter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogOutUseCase {

	private final LoginLogDao loginLogDao;
	private final LoginLogCommand loginLogCommand;

	private final ApplicationEventPublisher applicationEventPublisher;

	// security
	private final AuthTokenValidator authTokenValidator;
	private final BlackTokenAuthsCommand blackTokenAuthCommand;
	private final TokenCacheEvictCommand tokenCacheEvictCommand;
	private final EnrollBlackTokenCacheCommand enrollBlackTokenCacheCommand;

	@Transactional
	public void execute(LogOutUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final String accessToken = request.getAccessToken();
		final String refreshToken = request.getRefreshToken();

		authTokenValidator.execute(refreshToken, accessToken, memberId);

		Optional<LoginLogEntity> loginLogSource =
				loginLogDao.findTopByMemberIdAndStatusAndDeletedFalse(memberId, LoginStatus.LOGIN);

		if (loginLogSource.isEmpty()) {
			log.warn("login log not found. memberId: {}", memberId);
			return;
		}
		LoginLog loginLog = LoginLogConverter.from(loginLogSource.get());

		loginLogCommand.saveLogoutLog(loginLog);

		blackTokenAuthCommand.execute(accessToken, refreshToken);
		enrollBlackTokenCacheCommand.execute(accessToken, refreshToken);
		tokenCacheEvictCommand.execute(accessToken);

		publishEvent(memberId);
	}

	private void publishEvent(Long memberId) {
		applicationEventPublisher.publishEvent(LogOutEvent.builder().memberId(memberId).build());
	}
}
