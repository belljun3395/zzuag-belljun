package com.zzaug.api.domain.external.dao.log;

import com.zzaug.api.domain.entity.log.EmailAuthLogEntity;
import com.zzaug.api.domain.persistence.log.EmailAuthLogRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("!usecase-test")
@Repository
@RequiredArgsConstructor
public class EmailAuthLogDaoImpl implements EmailAuthLogDao {

	private final EmailAuthLogRepository emailAuthLogRepository;

	@Override
	public Optional<EmailAuthLogEntity> findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
			Long memberId, Long emailAuthId, String reason) {
		return emailAuthLogRepository.findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
				memberId, emailAuthId, reason);
	}

	@Override
	public EmailAuthLogEntity saveEmailAuthLog(EmailAuthLogEntity entity) {
		return emailAuthLogRepository.save(entity);
	}
}