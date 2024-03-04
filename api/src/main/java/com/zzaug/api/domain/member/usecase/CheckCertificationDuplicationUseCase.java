package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.member.AuthenticationDao;
import com.zzaug.api.domain.member.data.entity.member.CertificationData;
import com.zzaug.api.domain.member.dto.CheckCertificationDuplicationUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckCertificationDuplicationUseCaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckCertificationDuplicationUseCase {

	private final AuthenticationDao authenticationDao;

	@Transactional
	public CheckCertificationDuplicationUseCaseResponse execute(
			CheckCertificationDuplicationUseCaseRequest certification) {
		final CertificationData certificationData =
				CertificationData.builder().certification(certification.getCertification()).build();

		// 이미 존재하는 Certification인지 확인
		boolean isDuplicateCertification =
				authenticationDao.existsByCertificationAndDeletedFalse(certificationData);
		if (isDuplicateCertification) {
			return CheckCertificationDuplicationUseCaseResponse.builder().duplication(true).build();
		}
		return CheckCertificationDuplicationUseCaseResponse.builder().duplication(false).build();
	}
}
