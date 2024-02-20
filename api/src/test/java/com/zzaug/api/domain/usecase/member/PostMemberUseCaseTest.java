package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.dto.member.PostMemberUseCaseRequest;
import com.zzaug.api.domain.dto.member.PostMemberUseCaseResponse;
import com.zzaug.api.domain.usecase.AbstractUseCaseTest;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockAuthenticationDao;
import com.zzaug.api.domain.usecase.config.mock.repository.UMockMemberSourceDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = {ApiApp.class, UMockAuthenticationDao.class, UMockMemberSourceDao.class})
class PostMemberUseCaseTest extends AbstractUseCaseTest {

	@Autowired private PostMemberUseCase postMemberUseCase;

	@Autowired private PasswordEncoder passwordEncoder;

	@Test
	void 회원가입을_진행합니다() {
		PostMemberUseCaseRequest request =
				PostMemberUseCaseRequest.builder().certification("sample").password("123@abc").build();

		PostMemberUseCaseResponse response = postMemberUseCase.execute(request);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getMemberId()).isEqualTo(UMockMemberSourceDao.MEMBER_ID);
		Assertions.assertThat(response.getCertification()).isEqualTo(request.getCertification());
		String password = response.getPassword();
		Assertions.assertThat(passwordEncoder.matches(request.getPassword(), password)).isTrue();
	}
}
