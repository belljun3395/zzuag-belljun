package com.zzaug.api.domain.persistence.log;

import com.zzaug.api.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.entity.log.LoginLogEntity;
import com.zzaug.api.domain.entity.log.LoginStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface LoginLogRepository extends JpaRepository<LoginLogEntity, Long> {

	@DeletedFalse
	Optional<LoginLogEntity> findTopByMemberIdAndStatusAndDeletedFalse(
			Long memberId, LoginStatus status);
}
