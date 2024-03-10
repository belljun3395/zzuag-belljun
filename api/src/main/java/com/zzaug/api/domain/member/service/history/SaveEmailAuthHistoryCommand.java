package com.zzaug.api.domain.member.service.history;

import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.model.auth.TryCount;

public interface SaveEmailAuthHistoryCommand {

	EmailAuthHistoryEntity execute(TryCount tryCount, Long memberId, Long emailAuthId, String reason);
}
