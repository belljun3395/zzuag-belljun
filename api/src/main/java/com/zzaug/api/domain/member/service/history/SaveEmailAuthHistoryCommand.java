package com.zzaug.api.domain.member.service.history;

import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.model.auth.TryCountElement;

public interface SaveEmailAuthHistoryCommand {

	EmailAuthHistoryEntity execute(
			TryCountElement tryCount, Long memberId, Long emailAuthId, String reason);
}
