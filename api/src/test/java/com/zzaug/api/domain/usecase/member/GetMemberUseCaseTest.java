package com.zzaug.api.domain.usecase.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.GetMemberUseCaseRequest;
import com.zzaug.api.domain.dto.member.GetMemberUseCaseResponse;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockExternalContactDao;
import com.zzaug.api.domain.usecase.config.mock.service.UMockMemberSourceQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {ApiApp.class, UMockMemberSourceQuery.class, UMockExternalContactDao.class})
class GetMemberUseCaseTest extends AbstractUseCaseTest {

	@Autowired private GetMemberUseCase getMemberUseCase;

	@Test
	void 멤버_정보를_조회합니다() {
		Long memberId = 1L;
		GetMemberUseCaseRequest request =
				GetMemberUseCaseRequest.builder().queryMemberId(memberId).build();

		GetMemberUseCaseResponse response = getMemberUseCase.execute(request);

		Assertions.assertAll(
				() -> assertThat(response.getId()).isEqualTo(memberId),
				() -> assertThat(response.getEmail()).isEqualTo(UMockExternalContactDao.EMAIL),
				() -> assertThat(response.getGithub()).isEqualTo(UMockExternalContactDao.GITHUB));
	}
}
