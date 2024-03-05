package com.zzaug.api.domain.member.service.history;

import com.zzaug.api.domain.member.model.auth.TryCountElement;

public interface GetEmailAuthCheckTryCountService {

	TryCountElement execute(Long memberId, Long emailAuthId);
}
