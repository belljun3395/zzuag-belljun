package com.zzaug.api.domain.external.service.member;

import com.zzaug.api.common.persistence.support.transaction.UseCaseTransactional;
import com.zzaug.api.domain.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.external.dao.member.ExternalContactDao;
import com.zzaug.api.domain.model.member.GetMemberId;
import com.zzaug.api.domain.model.member.MemberContacts;
import com.zzaug.api.domain.support.entity.MemberContactExtractor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberContactsQueryImpl implements MemberContactsQuery {

	private final ExternalContactDao externalContactDao;

	@Override
	@UseCaseTransactional(readOnly = true)
	public MemberContacts execute(GetMemberId memberId) {
		List<ExternalContactEntity> contacts =
				externalContactDao.findAllByMemberIdAndDeletedFalse(memberId.getMemberId());
		return MemberContactExtractor.execute(contacts);
	}
}
