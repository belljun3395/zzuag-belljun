package com.zzaug.api.domain.external.security.auth;

public interface EnrollBlackTokenCacheCommand {

	void execute(String token);

	void execute(String accessToken, String refreshToken);
}
