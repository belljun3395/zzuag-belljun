package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.GetMemberUseCaseRequest;
import com.zzaug.api.domain.dto.member.GetMemberUseCaseResponse;
import com.zzaug.api.domain.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.external.dao.member.ExternalContactDao;
import com.zzaug.api.domain.external.service.member.MemberSourceQuery;
import com.zzaug.api.domain.model.member.MemberContacts;
import com.zzaug.api.domain.model.member.MemberSource;
import com.zzaug.api.domain.support.entity.MemberContactExtractor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMemberUseCase {

	private final MemberSourceQuery memberSourceQuery;

	private final ExternalContactDao externalContactRepository;

	@Transactional
	public GetMemberUseCaseResponse execute(GetMemberUseCaseRequest request) {
		final Long queryMemberId = request.getQueryMemberId();

		MemberSource source = memberSourceQuery.execute(queryMemberId);

		log.debug("Get member's contacts. memberId: {}", source.getId());
		List<ExternalContactEntity> contacts =
				externalContactRepository.findAllByMemberIdAndDeletedFalse(source.getId());
		MemberContacts memberContacts = MemberContactExtractor.execute(contacts);

		return GetMemberUseCaseResponse.builder()
				.id(source.getId())
				.email(memberContacts.getEmail())
				.github(memberContacts.getGithub())
				.build();
	}
}
