package com.zzaug.api.domain.member.service.history;

import static com.zzaug.api.domain.member.model.auth.EmailAuthResult.SUCCESS;

import com.zzaug.api.domain.member.dao.history.EmailAutHistoryDao;
import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.model.auth.TryCountElement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile("!usecase-test")
@Slf4j
@Service
@RequiredArgsConstructor
public class GetEmailAuthCheckTryCountServiceImpl implements GetEmailAuthCheckTryCountService {

	private final EmailAutHistoryDao emailAutHistoryDao;

	@Override
	@Transactional
	public TryCountElement execute(Long memberId, Long emailAuthId) {
		// 이메일 인증을 실패한 이력이 있는지 조회
		Optional<EmailAuthHistoryEntity> emailAuthLogSource =
				emailAutHistoryDao.findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
						memberId, emailAuthId, SUCCESS.getReason());
		if (emailAuthLogSource.isEmpty()) {
			return TryCountElement.newState();
		} else {
			return TryCountElement.builder()
					.tryCount(Math.toIntExact(emailAuthLogSource.get().getTryCount()))
					.emailAuthLogId(emailAuthLogSource.get().getId())
					.build();
		}
	}
}
