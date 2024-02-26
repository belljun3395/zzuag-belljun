package com.zzaug.api.domain.member.dao.member;

import com.zzaug.api.domain.member.data.entity.member.ContactType;
import com.zzaug.api.domain.member.data.entity.member.ExternalContactEntity;
import java.util.List;

public interface ExternalContactDao {

	boolean existsByContactTypeAndSourceAndDeletedFalse(ContactType type, String source);

	List<ExternalContactEntity> findAllByMemberIdAndDeletedFalse(Long memberId);

	ExternalContactEntity saveContact(ExternalContactEntity externalContactEntity);
}
