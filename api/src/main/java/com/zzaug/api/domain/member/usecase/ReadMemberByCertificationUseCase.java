package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.member.AuthenticationDao;
import com.zzaug.api.domain.member.dao.member.ExternalContactDao;
import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.data.entity.member.CertificationData;
import com.zzaug.api.domain.member.data.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.member.dto.ReadMemberByCertificationUseCaseRequest;
import com.zzaug.api.domain.member.dto.SearchMemberUseCaseResponse;
import com.zzaug.api.domain.member.model.member.MemberAuthentication;
import com.zzaug.api.domain.member.model.member.MemberContacts;
import com.zzaug.api.domain.member.util.entity.MemberAuthenticationConverter;
import com.zzaug.api.domain.member.util.entity.MemberContactExtractor;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadMemberByCertificationUseCase {

	private final AuthenticationDao authenticationDao;
	private final ExternalContactDao externalContactDao;

	@Transactional
	public SearchMemberUseCaseResponse execute(ReadMemberByCertificationUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();

		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByCertificationAndDeletedFalse(certification);
		if (authenticationSource.isEmpty()) {
			return SearchMemberUseCaseResponse.notExistSearchTarget();
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		List<ExternalContactEntity> contacts =
				externalContactDao.findAllByMemberIdAndDeletedFalse(memberAuthentication.getMemberId());
		MemberContacts memberContacts = MemberContactExtractor.execute(contacts);

		return SearchMemberUseCaseResponse.builder()
				.id(memberAuthentication.getMemberId())
				.certification(memberAuthentication.getCertification())
				.email(memberContacts.getEmail())
				.github(memberContacts.getGithub())
				.build();
	}
}
