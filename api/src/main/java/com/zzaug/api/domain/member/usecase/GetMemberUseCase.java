package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.member.ExternalContactDao;
import com.zzaug.api.domain.member.data.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.member.dto.GetMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.GetMemberUseCaseResponse;
import com.zzaug.api.domain.member.model.member.MemberContacts;
import com.zzaug.api.domain.member.model.member.MemberSource;
import com.zzaug.api.domain.member.service.member.GetMemberSourceQuery;
import com.zzaug.api.domain.member.util.entity.MemberContactExtractor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMemberUseCase {

	private final ExternalContactDao externalContactDao;

	private final GetMemberSourceQuery getMemberSourceQuery;

	@Transactional
	public GetMemberUseCaseResponse execute(GetMemberUseCaseRequest request) {
		final Long queryMemberId = request.getQueryMemberId();

		MemberSource source = getMemberSourceQuery.execute(queryMemberId);

		// 멤버의 외부 연락처 정보 조회
		List<ExternalContactEntity> contacts =
				externalContactDao.findAllByMemberIdAndDeletedFalse(source.getId());
		MemberContacts memberContacts = MemberContactExtractor.execute(contacts);

		// todo refactor: Certification도 포함될 수 있도록 수정
		return GetMemberUseCaseResponse.builder()
				.id(source.getId())
				.email(memberContacts.getEmail())
				.github(memberContacts.getGithub())
				.build();
	}
}
