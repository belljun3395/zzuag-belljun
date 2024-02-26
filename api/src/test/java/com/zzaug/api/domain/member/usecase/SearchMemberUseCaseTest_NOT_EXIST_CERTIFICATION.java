package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.SearchMemberUseCaseRequest;
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
class SearchMemberUseCaseTest_NOT_EXIST_CERTIFICATION extends AbstractUseCaseTest {

	@Autowired private SearchMemberUseCase searchMemberUseCase;

	@Test
	void 없는_증명을_통해_회원_검색_요청을_진행합니다() {
		// Given
		SearchMemberUseCaseRequest request =
				SearchMemberUseCaseRequest.builder().certification("notExist").build();

		// When
		Assertions.assertThatThrownBy(() -> searchMemberUseCase.execute(request))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
