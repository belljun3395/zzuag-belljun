package com.zzaug.api.domain.member.dao.member;

import com.zzaug.api.domain.member.data.entity.member.MemberEntity;
import com.zzaug.api.domain.member.data.entity.member.MemberStatus;
import java.util.Optional;

public interface MemberSourceDao {

	Optional<MemberEntity> findByIdAndStatusAndDeletedFalse(Long id, MemberStatus status);

	MemberEntity saveSource(MemberEntity memberEntity);
}
