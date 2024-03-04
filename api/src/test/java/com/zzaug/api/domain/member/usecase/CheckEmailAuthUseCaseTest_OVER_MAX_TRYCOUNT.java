package com.zzaug.api.domain.member.usecase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockEmailAuthDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockExternalContactDao;
import com.zzaug.api.domain.member.usecase.config.mock.service.UMcokGetEmailAuthCheckTryCountService;
import com.zzaug.api.domain.member.usecase.config.mock.service.UMockGetMemberSourceQuery;
import com.zzaug.api.domain.member.usecase.config.mock.service.UMockSaveEmailAuthHistoryCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"over-max-try-count"})
@SpringBootTest(
		classes = {
			ApiApp.class,
			UMockExternalContactDao.class,
			UMockEmailAuthDao.class,
			UMockGetMemberSourceQuery.class,
			UMcokGetEmailAuthCheckTryCountService.class,
			UMockSaveEmailAuthHistoryCommand.class,
		})
class CheckEmailAuthUseCaseTest_OVER_MAX_TRYCOUNT extends AbstractUseCaseTest {

	@Value("${token.test}")
	public String token;

	@Autowired private CheckEmailAuthUseCase checkEmailAuthUseCase;

	Long memberId = 1L;
	String code = UMockEmailAuthDao.CODE;
	String email = UMockEmailAuthDao.EMAIL;
	String nonce = "nonce";

	CheckEmailAuthUseCaseRequest request;

	@BeforeEach
	void setUp() {
		request =
				CheckEmailAuthUseCaseRequest.builder()
						.memberId(memberId)
						.code(code)
						.email(email)
						.nonce(nonce)
						.build();
	}

	@Test
	void 이메일_인증시도_초과로_인한_실패() {
		// Given

		// When
		CheckEmailAuthUseCaseResponse response = checkEmailAuthUseCase.execute(request);

		// Then
		org.junit.jupiter.api.Assertions.assertAll(
				() -> assertFalse(response.getAuthentication()),
				() ->
						assertThat(response.getTryCount())
								.isEqualTo(UMcokGetEmailAuthCheckTryCountService.MAX_TRY_COUNT));
	}
}
