package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.MemberAuthToken;
import com.zzaug.api.domain.dto.member.RenewalTokenUseCaseRequest;
import com.zzaug.api.domain.external.security.auth.ReplaceTokenCacheService;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockExternalContactDao;
import com.zzaug.api.domain.usecase.config.mock.security.UMockBlackTokenAuthCommand;
import com.zzaug.api.domain.usecase.validator.MemberAuthTokenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {
			ApiApp.class,
			UMockAuthenticationDao.class,
			UMockExternalContactDao.class,
			UMockBlackTokenAuthCommand.class,
			ReplaceTokenCacheService.class
		})
class RenewalTokenUseCaseTest extends AbstractUseCaseTest {

	@Value("${token.test}")
	public String token;

	@Autowired private RenewalTokenUseCase renewalTokenUseCase;
	@Autowired private MemberAuthTokenValidator memberAuthTokenValidator;

	@Test
	void 토큰_재발급_요청을_진행합니다() {
		RenewalTokenUseCaseRequest request =
				RenewalTokenUseCaseRequest.builder().refreshToken(token).accessToken(token).build();

		MemberAuthToken response = renewalTokenUseCase.execute(request);

		// Then
		memberAuthTokenValidator.delegate(response);
	}
}
