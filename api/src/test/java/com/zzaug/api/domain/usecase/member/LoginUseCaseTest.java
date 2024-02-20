package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.LoginUseCaseRequest;
import com.zzaug.api.domain.dto.member.MemberAuthToken;
import com.zzaug.api.domain.exception.PasswordNotMatchException;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockExternalContactDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockLoginLogDao;
import com.zzaug.api.domain.usecase.config.mock.security.UMockEnrollTokenCacheCommand;
import com.zzaug.api.domain.usecase.validator.MemberAuthTokenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {
			ApiApp.class,
			UMockAuthenticationDao.class,
			UMockExternalContactDao.class,
			UMockLoginLogDao.class,
			UMockEnrollTokenCacheCommand.class
		})
class LoginUseCaseTest extends AbstractUseCaseTest {

	@Autowired private LoginUseCase loginUseCase;
	@Autowired private MemberAuthTokenValidator memberAuthTokenValidator;

	@Test
	void 로그인을_진행합니다() {
		// Given
		LoginUseCaseRequest request =
				LoginUseCaseRequest.builder()
						.certification(UMockAuthenticationDao.CERTIFICATION)
						.password(UMockAuthenticationDao.PASSWORD_SOURCE)
						.userAgent("userAgent")
						.build();

		// When
		MemberAuthToken response = loginUseCase.execute(request);

		// Then
		memberAuthTokenValidator.delegate(response);
	}

	@Test
	void 비밀번호가_일치하지_않는다면_로그인에_실패합니다() {
		// Given
		LoginUseCaseRequest request =
				LoginUseCaseRequest.builder()
						.certification(UMockAuthenticationDao.CERTIFICATION)
						.password("wrongPassword")
						.userAgent("userAgent")
						.build();

		// When
		org.assertj.core.api.Assertions.assertThatThrownBy(() -> loginUseCase.execute(request))
				.isInstanceOf(PasswordNotMatchException.class);
	}
}
