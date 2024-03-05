package com.zzaug.api.domain.member.usecase.config.mock.service;

import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.model.auth.TryCountElement;
import com.zzaug.api.domain.member.service.history.SaveEmailAuthHistoryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Profile;

@Profile("usecase-test")
@TestComponent
@RequiredArgsConstructor
public class UMockSaveEmailAuthHistoryCommand implements SaveEmailAuthHistoryCommand {

	@Override
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
		return EmailAuthHistoryEntity.builder()
				.memberId(memberId)
				.emailAuthId(emailAuthId)
				.reason(reason)
				.tryCount(tryCount)
				.build();
	}

	private EmailAuthHistoryEntity save(
			Long id, Long memberId, Long emailAuthId, String reason, Long tryCount) {
		return EmailAuthHistoryEntity.builder()
				.id(id)
				.memberId(memberId)
				.emailAuthId(emailAuthId)
				.reason(reason)
				.tryCount(tryCount)
				.build();
	}
}
