package com.zzaug.api.domain.external.dao.log;

import com.zzaug.api.domain.entity.log.EmailAuthLogEntity;
import java.util.Optional;

public interface EmailAuthLogDao {
	Optional<EmailAuthLogEntity> findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
			Long memberId, Long emailAuthId, String reason);

	EmailAuthLogEntity saveEmailAuthLog(EmailAuthLogEntity entity);
}
