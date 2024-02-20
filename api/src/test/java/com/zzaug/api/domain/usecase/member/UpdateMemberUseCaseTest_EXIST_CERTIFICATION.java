package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.UpdateMemberUseCaseRequest;
import com.zzaug.api.domain.exception.ExistSourceException;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockMemberSourceDao;
import com.zzaug.api.domain.usecase.config.mock.service.UMockMemberSourceQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"exist-certification"})
@SpringBootTest(
		classes = {ApiApp.class, UMockAuthenticationDao.class, UMockMemberSourceQuery.class})
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
						.refreshToken(token)
						.accessToken(token)
						.build();

		Assertions.assertThatThrownBy(() -> updateMemberUseCase.execute(request))
				.isInstanceOf(ExistSourceException.class);
	}
}
