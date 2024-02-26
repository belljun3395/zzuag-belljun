package com.zzaug.api.domain.member.service.history;

import com.zzaug.api.domain.member.dao.history.EmailAutHistoryDao;
import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.model.auth.TryCountElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveEmailAuthHistoryCommand {

	private final EmailAutHistoryDao emailAutHistoryDao;

	@Transactional
	public EmailAuthHistoryEntity execute(
			TryCountElement tryCount, Long memberId, Long emailAuthId, String reason) {
		if (tryCount.isNew()) {
			return save(
					tryCount.getEmailAuthLogId(),
					memberId,
					emailAuthId,
					reason,
					(long) tryCount.getTryCount());
		} else {
			return save(memberId, emailAuthId, reason, (long) tryCount.getTryCount());
		}
	}

	private EmailAuthHistoryEntity save(
			Long memberId, Long emailAuthId, String reason, Long tryCount) {
		EmailAuthHistoryEntity emailAuthHistoryEntity =
				EmailAuthHistoryEntity.builder()
						.memberId(memberId)
						.emailAuthId(emailAuthId)
						.reason(reason)
						.tryCount(tryCount)
						.build();
		return emailAutHistoryDao.saveEmailAuthLog(emailAuthHistoryEntity);
	}

	private EmailAuthHistoryEntity save(
			Long id, Long memberId, Long emailAuthId, String reason, Long tryCount) {
		EmailAuthHistoryEntity emailAuthHistoryEntity =
				EmailAuthHistoryEntity.builder()
						.id(id)
						.memberId(memberId)
						.emailAuthId(emailAuthId)
						.reason(reason)
						.tryCount(tryCount)
						.build();
		return emailAutHistoryDao.saveEmailAuthLog(emailAuthHistoryEntity);
	}
}
