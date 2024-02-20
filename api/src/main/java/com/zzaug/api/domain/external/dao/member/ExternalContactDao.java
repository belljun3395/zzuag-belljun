package com.zzaug.api.domain.external.dao.member;

import com.zzaug.api.domain.entity.member.ContactType;
import com.zzaug.api.domain.entity.member.ExternalContactEntity;
import java.util.List;

public interface ExternalContactDao {

	boolean existsByContactTypeAndSourceAndDeletedFalse(ContactType type, String source);

	List<ExternalContactEntity> findAllByMemberIdAndDeletedFalse(Long memberId);

	ExternalContactEntity saveContact(ExternalContactEntity externalContactEntity);
}
