package com.zzaug.api.domain.external.security.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Profile("!usecase-test")
@Service
@RequiredArgsConstructor
public class ReplaceWhiteTokenCacheServiceImpl implements ReplaceTokenCacheService {

	@Value("${security.jwt.token.validtime.access}")
	private Long accessTokenValidTime;

	private final TokenCacheEvictCommand whiteTokenCacheEvictCommandImpl;
	private final EnrollWhiteTokenCacheCommand enrollWhiteTokenCacheCommand;
	private final EnrollBlackTokenCacheCommand enrollBlackTokenCacheCommand;

	@Override
	@Transactional
	public void execute(String oldToken, String newToken, Long memberId) {
		log.debug("Evict old token.\ntoken: {}", oldToken);
		whiteTokenCacheEvictCommandImpl.execute(oldToken);
		log.debug("Enroll old token to black list.\ntoken: {}", oldToken);
		enrollBlackTokenCacheCommand.execute(oldToken);
		log.debug("Enroll new token.\ntoken: {}", newToken);
		enrollWhiteTokenCacheCommand.execute(newToken, accessTokenValidTime, memberId);
	}
}
