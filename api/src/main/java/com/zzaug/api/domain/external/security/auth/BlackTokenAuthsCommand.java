package com.zzaug.api.domain.external.security.auth;

public interface BlackTokenAuthsCommand {

	void execute(String accessToken, String refreshToken);
}
