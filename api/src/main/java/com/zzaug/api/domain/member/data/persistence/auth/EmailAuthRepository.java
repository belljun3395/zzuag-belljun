package com.zzaug.api.domain.member.data.persistence.auth;

import com.zzaug.api.domain.common.persistence.support.checker.DeletedFalse;
import com.zzaug.api.domain.common.persistence.support.checker.ZzuagRepository;
import com.zzaug.api.domain.member.data.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.member.data.entity.auth.EmailData;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@ZzuagRepository
public interface EmailAuthRepository extends JpaRepository<EmailAuthEntity, Long> {

	@DeletedFalse
	Optional<EmailAuthEntity> findByMemberIdAndEmailAndNonceAndDeletedFalse(
			Long memberId, EmailData email, String nonce);
}
