package com.zzaug.api.domain.persistence.log;

import com.zzaug.api.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.entity.log.EmailAuthLogEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface EmailAuthLogRepository extends JpaRepository<EmailAuthLogEntity, Long> {

	@DeletedFalse
	Optional<EmailAuthLogEntity> findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
			Long memberId, Long emailAuthId, String reason);
}
