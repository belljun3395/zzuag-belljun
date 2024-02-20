package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.CheckDuplicationUseCaseRequest;
import com.zzaug.api.domain.dto.member.CheckDuplicationUseCaseResponse;
import com.zzaug.api.domain.entity.member.CertificationData;
import com.zzaug.api.domain.external.dao.member.AuthenticationDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckDuplicationUseCase {

	private final AuthenticationDao authenticationDao;

	@Transactional
	public CheckDuplicationUseCaseResponse execute(CheckDuplicationUseCaseRequest certification) {
		final CertificationData certificationData =
				CertificationData.builder().certification(certification.getCertification()).build();

		log.debug("Check duplication. certification: {}", certification.getCertification());
		boolean isDuplicateCertification =
				authenticationDao.existsByCertificationAndDeletedFalse(certificationData);
		if (isDuplicateCertification) {
			log.debug("Certification is duplicated. certification: {}", certification.getCertification());
			return CheckDuplicationUseCaseResponse.builder().duplication(true).build();
		}
		log.debug(
				"Certification is not duplicated. certification: {}", certification.getCertification());
		return CheckDuplicationUseCaseResponse.builder().duplication(false).build();
	}
}
