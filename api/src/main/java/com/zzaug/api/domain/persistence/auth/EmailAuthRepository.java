package com.zzaug.api.domain.persistence.auth;

import com.zzaug.api.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.entity.auth.EmailData;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface EmailAuthRepository extends JpaRepository<EmailAuthEntity, Long> {

	@DeletedFalse
	Optional<EmailAuthEntity> findByMemberIdAndEmailAndNonceAndDeletedFalse(
			Long memberId, EmailData email, String nonce);
}
