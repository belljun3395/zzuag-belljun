package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.SearchMemberUseCaseRequest;
import com.zzaug.api.domain.dto.member.SearchMemberUseCaseResponse;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockExternalContactDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {ApiApp.class, UMockAuthenticationDao.class, UMockExternalContactDao.class})
class SearchMemberUseCaseTest extends AbstractUseCaseTest {

	@Autowired private SearchMemberUseCase searchMemberUseCase;

	@Test
	void 증명을_통해_회원_검색_요청을_진행합니다() {
		// Given
		SearchMemberUseCaseRequest request =
				SearchMemberUseCaseRequest.builder()
						.certification(UMockAuthenticationDao.CERTIFICATION)
						.build();

		// When
		SearchMemberUseCaseResponse response = searchMemberUseCase.execute(request);

		// Then
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(response.getId()).isEqualTo(UMockAuthenticationDao.MEMBER_ID),
				() -> Assertions.assertThat(response.getEmail()).isEqualTo(UMockExternalContactDao.EMAIL),
				() ->
						Assertions.assertThat(response.getGithub()).isEqualTo(UMockExternalContactDao.GITHUB));
	}
}
