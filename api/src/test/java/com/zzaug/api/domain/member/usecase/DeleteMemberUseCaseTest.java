package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.DeleteMemberUseCaseRequest;
import com.zzaug.api.domain.member.usecase.config.mock.repository.UMockMemberSourceDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {
			ApiApp.class,
			UMockMemberSourceDao.class,
		})
class DeleteMemberUseCaseTest extends AbstractUseCaseTest {
	@Value("${token.test}")
	public String token;

	@Autowired private DeleteMemberUseCase deleteMemberUseCase;

	@Test
	void 회원을_탈퇴합니다() {
		DeleteMemberUseCaseRequest request =
				DeleteMemberUseCaseRequest.builder().memberId(UMockMemberSourceDao.MEMBER_ID).build();

		deleteMemberUseCase.execute(request);
	}
}
