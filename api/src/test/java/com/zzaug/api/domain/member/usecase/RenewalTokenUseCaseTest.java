package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.MemberAuthToken;
import com.zzaug.api.domain.member.dto.RenewalTokenUseCaseRequest;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockExternalContactDao;
import com.zzaug.api.domain.member.usecase.validator.MemberAuthTokenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {
			ApiApp.class,
			UMockAuthenticationDao.class,
			UMockExternalContactDao.class,
		})
class RenewalTokenUseCaseTest extends AbstractUseCaseTest {

	@Value("${token.test}")
	public String token;

	@Autowired private RenewalTokenUseCase renewalTokenUseCase;
	@Autowired private MemberAuthTokenValidator memberAuthTokenValidator;

	@Test
	void 토큰_재발급_요청을_진행합니다() {
		RenewalTokenUseCaseRequest request =
				RenewalTokenUseCaseRequest.builder().memberId(UMockAuthenticationDao.MEMBER_ID).build();

		MemberAuthToken response = renewalTokenUseCase.execute(request);

		// Then
		memberAuthTokenValidator.delegate(response);
	}
}
