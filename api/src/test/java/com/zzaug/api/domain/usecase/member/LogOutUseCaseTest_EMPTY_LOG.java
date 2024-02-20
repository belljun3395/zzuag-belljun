package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.LogOutUseCaseRequest;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockLoginLogDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"empty-login-log"})
@SpringBootTest(classes = {ApiApp.class, UMockLoginLogDao.class})
class LogOutUseCaseTest_EMPTY_LOG extends AbstractUseCaseTest {

	@Value("${token.test}")
	public String token;

	@Autowired private LogOutUseCase logOutUseCase;

	@Test
	void 로그인_로그가_없는_경우() {
		// Given
		LogOutUseCaseRequest request =
				LogOutUseCaseRequest.builder()
						.memberId(UMockAuthenticationDao.MEMBER_ID)
						.refreshToken(token)
						.accessToken(token)
						.build();

		// When
		logOutUseCase.execute(request);
	}
}
