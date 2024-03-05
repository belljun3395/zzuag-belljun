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

@SpringBootTest(
		classes = {ApiApp.class, UMockAuthenticationDao.class, UMockExternalContactDao.class})
class ReadMemberByCertificationUseCaseTest extends AbstractUseCaseTest {

	@Autowired private ReadMemberByCertificationUseCase readMemberByCertificationUseCase;

	@Test
	void 증명을_통해_회원_검색_요청을_진행합니다() {
		// Given
		ReadMemberByCertificationUseCaseRequest request =
				ReadMemberByCertificationUseCaseRequest.builder()
						.certification(UMockAuthenticationDao.CERTIFICATION)
						.build();

		// When
		SearchMemberUseCaseResponse response = readMemberByCertificationUseCase.execute(request);

		// Then
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(response.getId()).isEqualTo(UMockAuthenticationDao.MEMBER_ID),
				() -> Assertions.assertThat(response.getEmail()).isEqualTo(UMockExternalContactDao.EMAIL),
				() ->
						Assertions.assertThat(response.getGithub()).isEqualTo(UMockExternalContactDao.GITHUB));
	}
}
