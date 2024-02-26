package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.member.AuthenticationDao;
import com.zzaug.api.domain.member.dao.member.MemberSourceDao;
import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.data.entity.member.CertificationData;
import com.zzaug.api.domain.member.data.entity.member.MemberEntity;
import com.zzaug.api.domain.member.data.entity.member.PasswordData;
import com.zzaug.api.domain.member.dto.PostMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.PostMemberUseCaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostMemberUseCase {

	private final MemberSourceDao memberSourceDao;
	private final AuthenticationDao authenticationDao;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public PostMemberUseCaseResponse execute(PostMemberUseCaseRequest request) {
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		PasswordData password = PasswordData.builder().password(request.getPassword()).build();

		boolean isDuplicateId = authenticationDao.existsByCertificationAndDeletedFalse(certification);
		if (isDuplicateId) {
			throw new IllegalArgumentException();
		}

		password = encodePassword(password);

		// todo certification을 기준으로 락을 걸어 처리 해야 함
		MemberEntity memberSource = MemberEntity.builder().build();
		MemberEntity sourceEntity = memberSourceDao.saveSource(memberSource);
		Long memberId = sourceEntity.getId();
		AuthenticationEntity authenticationSource =
				AuthenticationEntity.builder()
						.memberId(memberId)
						.certification(certification)
						.password(password)
						.build();
		AuthenticationEntity authenticationEntity =
				authenticationDao.saveAuthentication(authenticationSource);

		return PostMemberUseCaseResponse.builder()
				.memberId(memberId)
				.certification(authenticationEntity.getCertification().getCertification())
				.password(authenticationEntity.getPassword().getPassword())
				.build();
	}

	private PasswordData encodePassword(PasswordData password) {
		String encodedPassword = passwordEncoder.encode(password.getPassword());
		return password.toBuilder().password(encodedPassword).build();
	}
}
