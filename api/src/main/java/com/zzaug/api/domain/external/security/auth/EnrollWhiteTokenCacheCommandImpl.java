package com.zzaug.api.domain.external.security.auth;

import com.zzaug.api.security.redis.auth.WhiteAuthTokenHash;
import com.zzaug.api.security.redis.auth.WhiteAuthTokenHashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Profile("!usecase-test")
@Component
@RequiredArgsConstructor
public class EnrollWhiteTokenCacheCommandImpl implements EnrollWhiteTokenCacheCommand {

	private final WhiteAuthTokenHashRepository whiteAuthTokenHashRepository;

	@Override
	@Transactional
	public void execute(String token, Long ttl, Long memberId) {
		whiteAuthTokenHashRepository.save(
				WhiteAuthTokenHash.builder().token(token).ttl(ttl).memberId(memberId).build());
	}
}
