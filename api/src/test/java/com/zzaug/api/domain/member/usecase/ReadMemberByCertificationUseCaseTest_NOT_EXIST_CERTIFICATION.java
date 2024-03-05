package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.ReadMemberByCertificationUseCaseRequest;
import com.zzaug.api.domain.member.dto.SearchMemberUseCaseResponse;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockExternalContactDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("not-exist-certification")
@SpringBootTest(
		classes = {ApiApp.class, UMockAuthenticationDao.class, UMockExternalContactDao.class})
class ReadMemberByCertificationUseCaseTest_NOT_EXIST_CERTIFICATION extends AbstractUseCaseTest {

	@Autowired private ReadMemberByCertificationUseCase readMemberByCertificationUseCase;

	@Test
	void 없는_증명을_통해_회원_검색_요청을_진행합니다() {
		// Given
		ReadMemberByCertificationUseCaseRequest request =
				ReadMemberByCertificationUseCaseRequest.builder().certification("notExist").build();

		// When
		SearchMemberUseCaseResponse response = readMemberByCertificationUseCase.execute(request);

		// Then
		Assertions.assertThat(response).isEqualTo(SearchMemberUseCaseResponse.notExistSearchTarget());
	}
}
