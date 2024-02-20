package com.zzaug.api.domain.external.security.auth;

public interface EnrollWhiteTokenCacheCommand {

	void execute(String token, Long ttl, Long memberId);
}
