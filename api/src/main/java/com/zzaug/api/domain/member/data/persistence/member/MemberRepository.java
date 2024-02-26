package com.zzaug.api.domain.member.data.persistence.member;

import com.zzaug.api.domain.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.domain.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.member.data.entity.member.MemberEntity;
import com.zzaug.api.domain.member.data.entity.member.MemberStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	@DeletedFalse
	Optional<MemberEntity> findByIdAndStatusAndDeletedFalse(Long id, MemberStatus status);
}
