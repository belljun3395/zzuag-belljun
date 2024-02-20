package com.zzaug.api.domain.usecase.member;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.CheckDuplicationUseCaseRequest;
import com.zzaug.api.domain.dto.member.CheckDuplicationUseCaseResponse;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockMemberSourceDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"exist-certification"})
@SpringBootTest(classes = {ApiApp.class, UMockAuthenticationDao.class, UMockMemberSourceDao.class})
class CheckDuplicationUseCaseTest_EXIST_CERTIFICATION extends AbstractUseCaseTest {

	@Autowired private CheckDuplicationUseCase checkDuplicationUseCase;

	@Test
	void 아이디가_중복되면_duplication이_true입니다() {
		// Given
		CheckDuplicationUseCaseRequest request =
				CheckDuplicationUseCaseRequest.builder().certification("sample").build();

		// When
		CheckDuplicationUseCaseResponse response = checkDuplicationUseCase.execute(request);

		// Then
		assertTrue(response.getDuplication());
	}
}
