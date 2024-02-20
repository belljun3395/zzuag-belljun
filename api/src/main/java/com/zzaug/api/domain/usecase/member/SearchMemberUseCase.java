package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.SearchMemberUseCaseRequest;
import com.zzaug.api.domain.dto.member.SearchMemberUseCaseResponse;
import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.CertificationData;
import com.zzaug.api.domain.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.SourceNotFoundException;
import com.zzaug.api.domain.external.dao.member.AuthenticationDao;
import com.zzaug.api.domain.external.dao.member.ExternalContactDao;
import com.zzaug.api.domain.model.member.GetMemberId;
import com.zzaug.api.domain.model.member.MemberContacts;
import com.zzaug.api.domain.support.entity.MemberAuthenticationConverter;
import com.zzaug.api.domain.support.entity.MemberContactExtractor;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchMemberUseCase {

	private final AuthenticationDao authenticationDao;
	private final ExternalContactDao externalContactDao;

	@Transactional
	public SearchMemberUseCaseResponse execute(SearchMemberUseCaseRequest request) {
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();

		log.debug("Get authentication. certification: {}", request.getCertification());
		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByCertificationAndDeletedFalse(certification);
		if (authenticationSource.isEmpty()) {
			throw new SourceNotFoundException(
					DBSource.AUTHENTICATION, "Certification", request.getCertification());
		}
		GetMemberId memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		log.debug("Get member contacts. memberId: {}", memberAuthentication.getMemberId());
		List<ExternalContactEntity> contacts =
				externalContactDao.findAllByMemberIdAndDeletedFalse(memberAuthentication.getMemberId());
		MemberContacts memberContacts = MemberContactExtractor.execute(contacts);

		return SearchMemberUseCaseResponse.builder()
				.id(memberAuthentication.getMemberId())
				.email(memberContacts.getEmail())
				.github(memberContacts.getGithub())
				.build();
	}
}
