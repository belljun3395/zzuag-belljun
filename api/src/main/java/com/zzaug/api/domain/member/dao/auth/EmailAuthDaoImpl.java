package com.zzaug.api.domain.member.dao.auth;

import com.zzaug.api.domain.member.data.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.member.data.entity.auth.EmailData;
import com.zzaug.api.domain.member.data.persistence.auth.EmailAuthRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Slf4j
@Profile("!usecase-test")
@Repository
@RequiredArgsConstructor
public class EmailAuthDaoImpl implements EmailAuthDao {

	private final EmailAuthRepository emailAuthRepository;

	@Override
	public Optional<EmailAuthEntity> findByMemberIdAndEmailAndNonceAndDeletedFalse(
			Long memberId, EmailData email, String nonce) {
		return emailAuthRepository.findByMemberIdAndEmailAndNonceAndDeletedFalse(
				memberId, email, nonce);
	}

	@Override
	public EmailAuthEntity saveEmailAuth(EmailAuthEntity emailAuthEntity) {
		return emailAuthRepository.save(emailAuthEntity);
	}
}
