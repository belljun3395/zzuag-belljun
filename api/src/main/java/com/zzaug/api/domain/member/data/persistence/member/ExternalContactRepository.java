package com.zzaug.api.domain.member.data.persistence.member;

import com.zzaug.api.domain.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.domain.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.member.data.entity.member.ContactType;
import com.zzaug.api.domain.member.data.entity.member.ExternalContactEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface ExternalContactRepository extends JpaRepository<ExternalContactEntity, Long> {

	@DeletedFalse
	boolean existsByContactTypeAndSourceAndDeletedFalse(ContactType type, String source);

	@DeletedFalse
	Optional<ExternalContactEntity> findByMemberIdAndContactTypeAndDeletedFalse(
			Long memberId, ContactType type);

	@DeletedFalse
	List<ExternalContactEntity> findAllByMemberIdAndDeletedFalse(Long memberId);
}
