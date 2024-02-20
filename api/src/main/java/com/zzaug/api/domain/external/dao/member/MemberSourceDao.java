package com.zzaug.api.domain.external.dao.member;

import com.zzaug.api.domain.entity.member.MemberEntity;
import com.zzaug.api.domain.entity.member.MemberStatus;
import java.util.Optional;

public interface MemberSourceDao {

	Optional<MemberEntity> findByIdAndStatusAndDeletedFalse(Long id, MemberStatus status);

	MemberEntity saveSource(MemberEntity memberEntity);
}
