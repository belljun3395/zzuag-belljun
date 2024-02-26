package com.zzaug.api.integration.member.config.encoder;

import com.zzaug.api.domain.member.config.encoder.ApiPasswordEncoder;

public class IntegrationTestApiBCryptPasswordEncoder implements ApiPasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.equals(encodedPassword);
	}
}
