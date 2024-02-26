package com.zzaug.api.domain.member.dao.history;

import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import java.util.Optional;

public interface EmailAutHistoryDao {

	Optional<EmailAuthHistoryEntity> findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
			Long memberId, Long emailAuthId, String reason);

	EmailAuthHistoryEntity saveEmailAuthLog(EmailAuthHistoryEntity entity);
}
