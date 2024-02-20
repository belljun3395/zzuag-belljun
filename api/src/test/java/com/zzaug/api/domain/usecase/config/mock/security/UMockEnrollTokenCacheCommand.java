package com.zzaug.api.domain.usecase.config.mock.security;

import com.zzaug.api.domain.external.security.auth.EnrollBlackTokenCacheCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Profile;

@Profile("usecase-test")
@TestComponent
@RequiredArgsConstructor
public class UMockEnrollTokenCacheCommand implements EnrollBlackTokenCacheCommand {

	@Override
	public void execute(String token) {
		// Do nothing
	}

	@Override
	public void execute(String accessToken, String refreshToken) {
		// Do nothing
	}
}
