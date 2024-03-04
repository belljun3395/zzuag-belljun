package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.config.encoder.ApiPasswordEncoder;
import com.zzaug.api.domain.member.dao.member.AuthenticationDao;
import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.data.entity.member.CertificationData;
import com.zzaug.api.domain.member.dto.UpdateMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.UpdateMemberUseCaseResponse;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthToken;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthTokenGenerator;
import com.zzaug.api.domain.member.model.member.MemberAuthentication;
import com.zzaug.api.domain.member.model.member.MemberSource;
import com.zzaug.api.domain.member.service.member.GetMemberSourceQuery;
import com.zzaug.api.domain.member.util.entity.MemberAuthenticationConverter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateMemberUseCase {

	private final AuthenticationDao authenticationDao;

	private final GetMemberSourceQuery getMemberSourceQuery;

	// security
	private final ApiPasswordEncoder passwordEncoder;
	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public UpdateMemberUseCaseResponse execute(UpdateMemberUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		final String password = request.getPassword();

		MemberSource memberSource = getMemberSourceQuery.execute(memberId);

		// 멤버 Id를 기준으로 인증 정보 조회
		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByMemberIdAndDeletedFalse(memberSource.getId());
		if (authenticationSource.isEmpty()) {
			// todo refactor: 서버, 프론트간 비정상적인 요청 예외로 처리
			throw new IllegalArgumentException();
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		// 비밀번호 일치 여부 확인
		if (!memberAuthentication.isMatchPassword(passwordEncoder, password)) {
			throw new IllegalArgumentException();
		}

		// 인증 정보 업데이트
		if (!memberAuthentication.isSameCertification(certification.getCertification())) {
			// todo false이면 Certification을 기준으로 락을 걸어 처리 해야 함
			boolean isDuplicateId = authenticationDao.existsByCertificationAndDeletedFalse(certification);
			if (isDuplicateId) {
				throw new IllegalArgumentException();
			}
			memberAuthentication.updateCertification(certification.getCertification());
		}

		// 멤버의 정보가 수정되었기에 새로운 토큰을 발급
		RoleUserAuthToken authToken = roleUserAuthTokenGenerator.generateToken(memberSource.getId());

		return UpdateMemberUseCaseResponse.builder()
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}
}
