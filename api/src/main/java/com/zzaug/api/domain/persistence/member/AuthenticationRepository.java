package com.zzaug.api.domain.persistence.member;

import com.zzaug.api.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.CertificationData;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface AuthenticationRepository extends JpaRepository<AuthenticationEntity, Long> {

	@DeletedFalse
	boolean existsByCertificationAndDeletedFalse(CertificationData certification);

	@DeletedFalse
	Optional<AuthenticationEntity> findByCertificationAndDeletedFalse(
			CertificationData certification);

	@DeletedFalse
	Optional<AuthenticationEntity> findByMemberIdAndDeletedFalse(Long memberId);
}
