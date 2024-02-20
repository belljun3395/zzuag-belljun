package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.PostMemberUseCaseRequest;
import com.zzaug.api.domain.dto.member.PostMemberUseCaseResponse;
import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.CertificationData;
import com.zzaug.api.domain.entity.member.MemberEntity;
import com.zzaug.api.domain.entity.member.PasswordData;
import com.zzaug.api.domain.event.member.SaveMemberEvent;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.ExistSourceException;
import com.zzaug.api.domain.external.dao.member.AuthenticationDao;
import com.zzaug.api.domain.external.dao.member.MemberSourceDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public PostMemberUseCaseResponse execute(PostMemberUseCaseRequest request) {
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		PasswordData password = PasswordData.builder().password(request.getPassword()).build();

		log.debug("Check duplicate certification. certification: {}", certification.getCertification());
		boolean isDuplicateId = authenticationDao.existsByCertificationAndDeletedFalse(certification);
		if (isDuplicateId) {
			throw new ExistSourceException(DBSource.AUTHENTICATION, certification.getCertification());
		}

		password = encodePassword(password);

		// todo certification을 기준으로 락을 걸어 처리 해야 함
		MemberEntity memberSource = MemberEntity.builder().build();
		log.debug("Save member source. memberSource: {}", memberSource);
		MemberEntity sourceEntity = memberSourceDao.saveSource(memberSource);
		Long memberId = sourceEntity.getId();
		log.debug("Saved member source id : {}", memberId);
		AuthenticationEntity authenticationSource =
				AuthenticationEntity.builder()
						.memberId(memberId)
						.certification(certification)
						.password(password)
						.build();
		log.debug("Save authentication source. authenticationSource: {}", authenticationSource);
		AuthenticationEntity authenticationEntity =
				authenticationDao.saveAuthentication(authenticationSource);

		publishEvent(memberId, certification);

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

	private void publishEvent(Long memberId, CertificationData certification) {
		log.debug("Publish save member event. memberId: {}");
		applicationEventPublisher.publishEvent(
				SaveMemberEvent.builder()
						.memberId(memberId)
						.memberCertification(certification.getCertification())
						.build());
	}
}
