package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.DeleteMemberUseCaseRequest;
import com.zzaug.api.domain.entity.member.MemberEntity;
import com.zzaug.api.domain.entity.member.MemberStatus;
import com.zzaug.api.domain.event.member.WithDrawnMemberEvent;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.SourceNotFoundException;
import com.zzaug.api.domain.external.dao.member.MemberSourceDao;
import com.zzaug.api.domain.external.security.AuthTokenValidator;
import com.zzaug.api.domain.external.security.auth.BlackTokenAuthsCommand;
import com.zzaug.api.domain.external.security.auth.EnrollBlackTokenCacheCommand;
import com.zzaug.api.domain.external.security.auth.TokenCacheEvictCommand;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMemberUseCase {

	private final MemberSourceDao memberSourceDao;

	private final ApplicationEventPublisher applicationEventPublisher;

	// security
	private final AuthTokenValidator authTokenValidator;
	private final BlackTokenAuthsCommand blackTokenAuthCommand;
	private final TokenCacheEvictCommand tokenCacheEvictCommand;
	private final EnrollBlackTokenCacheCommand enrollBlackTokenCacheCommand;

	@Transactional
	public void execute(DeleteMemberUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final String accessToken = request.getAccessToken();
		final String refreshToken = request.getRefreshToken();

		authTokenValidator.execute(refreshToken, accessToken, memberId);

		log.debug("Get member. memberId: {}", memberId);
		Optional<MemberEntity> memberSource =
				memberSourceDao.findByIdAndStatusAndDeletedFalse(memberId, MemberStatus.REGULAR);
		if (memberSource.isEmpty()) {
			throw new SourceNotFoundException(DBSource.MEMBER, memberId);
		}

		MemberEntity memberEntity = memberSource.get();
		MemberEntity withdrawnMember = memberEntity.toBuilder().status(MemberStatus.WITHDRAWN).build();
		log.debug("Save member status to withdrawn. memberId: {}", memberId);
		memberSourceDao.saveSource(withdrawnMember);

		blackTokenAuthCommand.execute(accessToken, refreshToken);
		enrollBlackTokenCacheCommand.execute(accessToken, refreshToken);
		tokenCacheEvictCommand.execute(accessToken);
		publishEvent(memberEntity);
	}

	private void publishEvent(MemberEntity memberEntity) {
		applicationEventPublisher.publishEvent(
				WithDrawnMemberEvent.builder().memberId(memberEntity.getId()).build());
	}
}
