package com.zzaug.api.domain.external.security.auth;

import com.zzaug.api.security.redis.auth.BlackAuthTokenHash;
import com.zzaug.api.security.redis.auth.BlackAuthTokenHashRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Profile("!usecase-test")
@Component
@RequiredArgsConstructor
public class EnrollBlackTokenCacheCommandImpl implements EnrollBlackTokenCacheCommand {

	private final BlackAuthTokenHashRepository blackAuthTokenHashRepository;

	@Override
	@Transactional
	public void execute(String token) {
		blackAuthTokenHashRepository.save(BlackAuthTokenHash.builder().token(token).build());
	}

	@Override
	public void execute(String accessToken, String refreshToken) {
		blackAuthTokenHashRepository.saveAll(
				List.of(
						BlackAuthTokenHash.builder().token(accessToken).build(),
						BlackAuthTokenHash.builder().token(refreshToken).build()));
	}
}
