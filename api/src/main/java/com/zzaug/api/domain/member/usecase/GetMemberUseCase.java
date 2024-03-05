package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.member.AuthenticationDao;
import com.zzaug.api.domain.member.dao.member.ExternalContactDao;
import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.data.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.member.dto.GetMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.GetMemberUseCaseResponse;
import com.zzaug.api.domain.member.exception.argument.NotMatchMemberException;
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
public class GetMemberUseCase {

	private final ExternalContactDao externalContactDao;

	private final AuthenticationDao authenticationDao;

	@Transactional
	public GetMemberUseCaseResponse execute(GetMemberUseCaseRequest request) {
		final Long queryMemberId = request.getQueryMemberId();

		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByMemberIdAndDeletedFalse(queryMemberId);
		if (authenticationSource.isEmpty()) {
			throw new NotMatchMemberException();
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		// 멤버의 외부 연락처 정보 조회
		List<ExternalContactEntity> contacts =
				externalContactDao.findAllByMemberIdAndDeletedFalse(memberAuthentication.getId());
		MemberContacts memberContacts = MemberContactExtractor.execute(contacts);

		return GetMemberUseCaseResponse.builder()
				.id(memberAuthentication.getId())
				.certification(memberAuthentication.getCertification())
				.email(memberContacts.getEmail())
				.github(memberContacts.getGithub())
				.build();
	}
}
