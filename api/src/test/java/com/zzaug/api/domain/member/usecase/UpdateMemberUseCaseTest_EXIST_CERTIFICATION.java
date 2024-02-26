package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.UpdateMemberUseCaseRequest;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockMemberSourceDao;
import com.zzaug.api.domain.member.usecase.config.mock.service.UMockGetMemberSourceQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"exist-certification"})
@SpringBootTest(
		classes = {ApiApp.class, UMockAuthenticationDao.class, UMockGetMemberSourceQuery.class})
class UpdateMemberUseCaseTest_EXIST_CERTIFICATION extends AbstractUseCaseTest {

	@Value("${token.test}")
	public String token;

	@Autowired private UpdateMemberUseCase updateMemberUseCase;

	@Test
	void 수정하려는_CERTIFICATION이_존재하면_수정할_수_없습니다() {
		UpdateMemberUseCaseRequest request =
				UpdateMemberUseCaseRequest.builder()
						.memberId(UMockMemberSourceDao.MEMBER_ID)
						.certification(UMockAuthenticationDao.CERTIFICATION + "edit")
						.password(UMockAuthenticationDao.PASSWORD_SOURCE)
						.build();

		Assertions.assertThatThrownBy(() -> updateMemberUseCase.execute(request))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
