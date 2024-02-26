package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.LoginUseCaseRequest;
import com.zzaug.api.domain.member.dto.MemberAuthToken;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockExternalContactDao;
import com.zzaug.api.domain.member.usecase.validator.MemberAuthTokenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {
			ApiApp.class,
			UMockAuthenticationDao.class,
			UMockExternalContactDao.class,
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
						.build();

		// When
		org.assertj.core.api.Assertions.assertThatThrownBy(() -> loginUseCase.execute(request))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
