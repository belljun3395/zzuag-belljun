package com.zzaug.api.domain.member.usecase.config.mock.service;

import com.zzaug.api.domain.member.model.auth.TryCountElement;
import com.zzaug.api.domain.member.service.history.GetEmailAuthCheckTryCountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;

/**
 * 테스트용 이메일 인증 확인 시도 횟수 조회 서비스
 *
 * <p>프로파일이 설정되지 않은 경우 인증 시도 횟수가 0회입니다.
 *
 * <p>프로파일이 over-max-try-count인 경우 인증 시도 횟수가 3회입니다.
 *
 * <p>프로파일이 under-max-try-count인 경우 인증 시도 횟수가 2회입니다.
 */
@Profile("usecase-test")
@TestComponent
@RequiredArgsConstructor
public class UMcokGetEmailAuthCheckTryCountService
		implements GetEmailAuthCheckTryCountService, ApplicationContextAware {

	public static final Long EMAIL_AUTH_LOG_ID = 1L;

	public static final Long INITIAL_TRY_COUNT = 0L;
	public static final Long UNDER_MAX_TRY_COUNT = 2L;
	public static final Long MAX_TRY_COUNT = 3L;

	private List<String> activeProfiles;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		activeProfiles = List.of(applicationContext.getEnvironment().getActiveProfiles());
	}

	@Override
	public TryCountElement execute(Long memberId, Long emailAuthId) {
		if (activeProfiles.contains("under-max-try-count")) {
			return TryCountElement.builder()
					.tryCount(Math.toIntExact(UNDER_MAX_TRY_COUNT))
					.emailAuthLogId(EMAIL_AUTH_LOG_ID)
					.build();
		}
		if (activeProfiles.contains("over-max-try-count")) {
			return TryCountElement.builder()
					.tryCount(Math.toIntExact(MAX_TRY_COUNT))
					.emailAuthLogId(EMAIL_AUTH_LOG_ID)
					.build();
		}
		return TryCountElement.builder().tryCount(Math.toIntExact(INITIAL_TRY_COUNT)).build();
	}
}
