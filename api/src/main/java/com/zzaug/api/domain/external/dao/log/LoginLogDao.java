package com.zzaug.api.domain.external.dao.log;

import com.zzaug.api.domain.entity.log.LoginLogEntity;
import com.zzaug.api.domain.entity.log.LoginStatus;
import java.util.Optional;

public interface LoginLogDao {

	LoginLogEntity saveLoginLog(LoginLogEntity entity);

	Optional<LoginLogEntity> findTopByMemberIdAndStatusAndDeletedFalse(
			Long memberId, LoginStatus status);
}
