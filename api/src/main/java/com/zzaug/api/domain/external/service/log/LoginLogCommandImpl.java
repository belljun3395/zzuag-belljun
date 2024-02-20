package com.zzaug.api.domain.external.service.log;

import com.zzaug.api.common.persistence.support.transaction.UseCaseTransactional;
import com.zzaug.api.domain.entity.log.LoginLogEntity;
import com.zzaug.api.domain.entity.log.LoginStatus;
import com.zzaug.api.domain.external.dao.log.LoginLogDao;
import com.zzaug.api.domain.model.log.LoginLog;
import com.zzaug.api.domain.support.entity.LoginLogConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogCommandImpl implements LoginLogCommand {

	private final LoginLogDao loginLogDao;

	@Override
	@UseCaseTransactional
	public void saveLoginLog(Long memberId, String userAgent) {
		loginLogDao.saveLoginLog(
				LoginLogEntity.builder()
						.status(LoginStatus.LOGIN)
						.memberId(memberId)
						.userAgent(userAgent)
						.build());
	}

	@Override
	public void saveLogoutLog(LoginLog loginLog) {
		LoginLogEntity loginLogEntity = LoginLogConverter.to(loginLog);
		loginLogDao.saveLoginLog(loginLogEntity);
	}
}
