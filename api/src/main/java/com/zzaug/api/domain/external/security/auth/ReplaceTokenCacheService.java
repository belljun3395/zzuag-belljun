package com.zzaug.api.domain.external.security.auth;

public interface ReplaceTokenCacheService {

	void execute(String oldToken, String newToken, Long memberId);
}
