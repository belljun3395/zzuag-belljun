package com.zzaug.api.domain.member.usecase;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.EmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.EmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockEmailAuthDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockExternalContactDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"exist-contact"})
@SpringBootTest(classes = {ApiApp.class, UMockExternalContactDao.class, UMockEmailAuthDao.class})
class EmailAuthUseCaseTest_EXIST_CONTACT extends AbstractUseCaseTest {

	@Autowired private EmailAuthUseCase emailAuthUseCase;

	@Test
	void 이메일_인증_요청() {
		// Given
		Long memberId = 1L;
		String email = "sample@email.com";
		String nonce = "abcdefgh";
		EmailAuthUseCaseRequest request =
				EmailAuthUseCaseRequest.builder().memberId(memberId).email(email).nonce(nonce).build();

		// When
		EmailAuthUseCaseResponse response = emailAuthUseCase.execute(request);

		// Then
		assertTrue(response.getDuplication());
	}
}
