package com.zzaug.api.domain.external.security.auth;

public interface TokenCacheEvictCommand {

	void execute(String token);
}
