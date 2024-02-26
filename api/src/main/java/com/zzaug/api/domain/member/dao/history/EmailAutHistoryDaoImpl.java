package com.zzaug.api.domain.member.dao.history;

import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.data.persistence.history.EmailAuthHistoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("!usecase-test")
@Repository
@RequiredArgsConstructor
public class EmailAutHistoryDaoImpl implements EmailAutHistoryDao {

	private final EmailAuthHistoryRepository emailAuthHistoryRepository;

	@Override
	public Optional<EmailAuthHistoryEntity> findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
			Long memberId, Long emailAuthId, String reason) {
		return emailAuthHistoryRepository.findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
				memberId, emailAuthId, reason);
	}

	@Override
	public EmailAuthHistoryEntity saveEmailAuthLog(EmailAuthHistoryEntity entity) {
		return emailAuthHistoryRepository.save(entity);
	}
}
