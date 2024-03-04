package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.config.encoder.ApiPasswordEncoder;
import com.zzaug.api.domain.member.dao.member.AuthenticationDao;
import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.data.entity.member.CertificationData;
import com.zzaug.api.domain.member.data.entity.member.PasswordData;
import com.zzaug.api.domain.member.dto.LoginUseCaseRequest;
import com.zzaug.api.domain.member.dto.MemberAuthToken;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthToken;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthTokenGenerator;
import com.zzaug.api.domain.member.model.member.MemberAuthentication;
import com.zzaug.api.domain.member.util.entity.MemberAuthenticationConverter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUseCase {

	private final AuthenticationDao authenticationDao;

	private final ApiPasswordEncoder passwordEncoder;
	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public MemberAuthToken execute(LoginUseCaseRequest request) {
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		final PasswordData password = PasswordData.builder().password(request.getPassword()).build();

		// Certification을 통해 인증 정보 조회
		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByCertificationAndDeletedFalse(certification);
		if (authenticationSource.isEmpty()) {
			throw new IllegalArgumentException();
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		// 비밀번호 일치 여부 확인
		if (!memberAuthentication.isMatchPassword(passwordEncoder, password.getPassword())) {
			throw new IllegalArgumentException();
		}

		// 인증 토큰 생성
		RoleUserAuthToken authToken =
				roleUserAuthTokenGenerator.generateToken(memberAuthentication.getMemberId());

		return MemberAuthToken.builder()
				.memberId(memberAuthentication.getMemberId())
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}
}
