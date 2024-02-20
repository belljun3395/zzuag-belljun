package com.zzaug.api.domain.external.security.auth;

import com.zzaug.api.security.redis.auth.WhiteAuthTokenHash;
import com.zzaug.api.security.redis.auth.WhiteAuthTokenHashRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Profile("!usecase-test")
@Component
@RequiredArgsConstructor
public class WhiteTokenCacheEvictCommandImpl implements TokenCacheEvictCommand {

	private final WhiteAuthTokenHashRepository whiteAuthTokenHashRepository;

	@Override
	@Transactional
	public void execute(String token) {
		Optional<WhiteAuthTokenHash> authTokenSource = whiteAuthTokenHashRepository.findByToken(token);
		if (authTokenSource.isEmpty()) {
			log.warn("auth token not found. token: {}", token);
			return;
		}
		WhiteAuthTokenHash whiteAuthTokenHash = authTokenSource.get();
		whiteAuthTokenHashRepository.deleteById(whiteAuthTokenHash.getId());
	}
}
