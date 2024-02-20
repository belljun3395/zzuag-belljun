package com.zzaug.api.domain.external.service.log;

import com.zzaug.api.domain.model.log.LoginLog;

public interface LoginLogCommand {

	void saveLoginLog(Long memberId, String userAgent);

	void saveLogoutLog(LoginLog loginLog);
}
