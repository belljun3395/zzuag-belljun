package com.zzaug.api.domain.external.dao.auth;

import com.zzaug.api.domain.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.entity.auth.EmailData;
import com.zzaug.api.domain.redis.email.EmailAuthSession;
import java.util.Optional;

public interface EmailAuthDao {
	Optional<EmailAuthEntity> findByMemberIdAndEmailAndNonceAndDeletedFalse(
			Long memberId, EmailData email, String nonce);

	EmailAuthEntity saveEmailAuth(EmailAuthEntity emailAuthEntity);

	Optional<EmailAuthSession> findBySessionId(String sessionId);

	EmailAuthSession saveEmailAuthSession(EmailAuthSession emailAuthSession);

	void deleteBySessionId(String sessionId);
}
