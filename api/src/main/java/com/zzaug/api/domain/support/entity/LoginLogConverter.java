package com.zzaug.api.domain.support.entity;

import com.zzaug.api.domain.entity.log.LoginLogEntity;
import com.zzaug.api.domain.entity.log.LoginStatus;
import com.zzaug.api.domain.model.log.LoginLog;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoginLogConverter {

	public LoginLog from(LoginLogEntity entity) {
		LoginStatus status = entity.getStatus();
		boolean isLogin = false;
		if (status.equals(LoginStatus.LOGIN)) {
			isLogin = true;
		}
		return LoginLog.builder()
				.id(entity.getId())
				.memberId(entity.getMemberId())
				.isLogin(isLogin)
				.userAgent(entity.getUserAgent())
				.build();
	}

	public LoginLogEntity to(LoginLog source) {
		LoginStatus status = LoginStatus.LOGOUT;
		if (source.isLogin()) {
			status = LoginStatus.LOGIN;
		}
		return LoginLogEntity.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.status(status)
				.userAgent(source.getUserAgent())
				.build();
	}
}
