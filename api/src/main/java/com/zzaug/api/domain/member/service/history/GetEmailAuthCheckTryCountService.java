package com.zzaug.api.domain.member.service.history;

import com.zzaug.api.domain.member.model.auth.TryCount;

public interface GetEmailAuthCheckTryCountService {

	TryCount execute(Long memberId, Long emailAuthId);
}
