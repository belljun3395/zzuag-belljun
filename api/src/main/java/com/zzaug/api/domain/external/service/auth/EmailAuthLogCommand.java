package com.zzaug.api.domain.external.service.auth;

import com.zzaug.api.domain.entity.log.EmailAuthLogEntity;

public interface EmailAuthLogCommand {

	EmailAuthLogEntity execute(
			Long id, Long memberId, Long emailAuthId, String reason, Long tryCount);

	EmailAuthLogEntity execute(Long memberId, Long emailAuthId, String reason, Long tryCount);
}
