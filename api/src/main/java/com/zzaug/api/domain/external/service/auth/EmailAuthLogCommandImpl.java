package com.zzaug.api.domain.external.service.auth;

import com.zzaug.api.common.persistence.support.transaction.UseCaseTransactional;
import com.zzaug.api.domain.entity.log.EmailAuthLogEntity;
import com.zzaug.api.domain.external.dao.log.EmailAuthLogDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAuthLogCommandImpl implements EmailAuthLogCommand {

	private final EmailAuthLogDao emailAuthLogDao;

	@Override
	@UseCaseTransactional
	public EmailAuthLogEntity execute(
			Long id, Long memberId, Long emailAuthId, String reason, Long tryCount) {
		EmailAuthLogEntity emailAuthLogEntity =
				EmailAuthLogEntity.builder()
						.id(id)
						.memberId(memberId)
						.emailAuthId(emailAuthId)
						.reason(reason)
						.tryCount(tryCount)
						.build();
		return emailAuthLogDao.saveEmailAuthLog(emailAuthLogEntity);
	}

	@Override
	@UseCaseTransactional
	public EmailAuthLogEntity execute(Long memberId, Long emailAuthId, String reason, Long tryCount) {
		EmailAuthLogEntity emailAuthLogEntity =
				EmailAuthLogEntity.builder()
						.memberId(memberId)
						.emailAuthId(emailAuthId)
						.reason(reason)
						.tryCount(tryCount)
						.build();
		return emailAuthLogDao.saveEmailAuthLog(emailAuthLogEntity);
	}
}
