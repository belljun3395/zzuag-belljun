package com.zzaug.api.domain.member.data.persistence.history;

import com.zzaug.api.domain.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.domain.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface EmailAuthHistoryRepository extends JpaRepository<EmailAuthHistoryEntity, Long> {

	@DeletedFalse
	Optional<EmailAuthHistoryEntity> findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
			Long memberId, Long emailAuthId, String reason);
}
