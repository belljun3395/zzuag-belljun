package com.zzaug.api.domain.member.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.CheckCertificationDuplicationUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckCertificationDuplicationUseCaseResponse;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockMemberSourceDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ApiApp.class, UMockAuthenticationDao.class, UMockMemberSourceDao.class})
class CheckCertificationDuplicationUseCaseTest extends AbstractUseCaseTest {

	@Autowired private CheckCertificationDuplicationUseCase checkCertificationDuplicationUseCase;

	@Test
	void 아이디가_중복되면_duplication이_false입니다() {
		// Given
		CheckCertificationDuplicationUseCaseRequest request =
				CheckCertificationDuplicationUseCaseRequest.builder().certification("sample").build();

		// When
		CheckCertificationDuplicationUseCaseResponse response =
				checkCertificationDuplicationUseCase.execute(request);

		// Then
		assertFalse(response.getDuplication());
	}
}
