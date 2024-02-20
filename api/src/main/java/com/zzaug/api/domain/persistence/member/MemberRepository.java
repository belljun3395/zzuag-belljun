package com.zzaug.api.domain.persistence.member;

import com.zzaug.api.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.entity.member.MemberEntity;
import com.zzaug.api.domain.entity.member.MemberStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	@DeletedFalse
	Optional<MemberEntity> findByIdAndStatusAndDeletedFalse(Long id, MemberStatus status);
}
