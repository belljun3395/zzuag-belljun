package com.zzaug.api.domain.member.dao.auth;

import com.zzaug.api.domain.member.data.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.member.data.entity.auth.EmailData;
import java.util.Optional;

public interface EmailAuthDao {

	Optional<EmailAuthEntity> findByMemberIdAndEmailAndNonceAndDeletedFalse(
			Long memberId, EmailData email, String nonce);

	EmailAuthEntity saveEmailAuth(EmailAuthEntity emailAuthEntity);
}
