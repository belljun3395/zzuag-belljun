package com.zzaug.api.domain.member.usecase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockEmailAutHistoryDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockEmailAuthDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockExternalContactDao;
import com.zzaug.api.domain.member.usecase.config.mock.service.UMockGetMemberSourceQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"under-max-try-count"})
@SpringBootTest(
		classes = {
			ApiApp.class,
			UMockExternalContactDao.class,
			UMockEmailAuthDao.class,
			UMockEmailAutHistoryDao.class,
			UMockGetMemberSourceQuery.class
		})
class CheckEmailAuthUseCaseTest_UNDER_MAX_TRYCOUNT extends AbstractUseCaseTest {

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
	void 이메일_인증_성공() {
		// Given

		// When
		CheckEmailAuthUseCaseResponse response = checkEmailAuthUseCase.execute(request);

		// Then
		assertTrue(response.getAuthentication());
		Assertions.assertThat(response.getTryCount())
				.isEqualTo(UMockEmailAutHistoryDao.UNDER_MAX_TRY_COUNT);
	}

	@Test
	void 코드_불일치로인한_이메일_인증_실패() {
		// Given
		CheckEmailAuthUseCaseRequest wrongCodeRequest =
				CheckEmailAuthUseCaseRequest.builder()
						.memberId(memberId)
						.code("wrong code")
						.email(email)
						.nonce(nonce)
						.build();

		// When
		CheckEmailAuthUseCaseResponse response = checkEmailAuthUseCase.execute(wrongCodeRequest);

		// Then
		assertFalse(response.getAuthentication());
		Assertions.assertThat(response.getTryCount())
				.isEqualTo(UMockEmailAutHistoryDao.UNDER_MAX_TRY_COUNT + 1);
	}
}
