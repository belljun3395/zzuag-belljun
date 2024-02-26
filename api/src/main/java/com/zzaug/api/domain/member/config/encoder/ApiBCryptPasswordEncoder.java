package com.zzaug.api.domain.member.config.encoder;

import java.security.SecureRandom;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ApiBCryptPasswordEncoder extends BCryptPasswordEncoder implements ApiPasswordEncoder {

	public ApiBCryptPasswordEncoder() {}

	public ApiBCryptPasswordEncoder(int strength) {
		super(strength);
	}

	public ApiBCryptPasswordEncoder(BCryptVersion version) {
		super(version);
	}

	public ApiBCryptPasswordEncoder(BCryptVersion version, SecureRandom random) {
		super(version, random);
	}

	public ApiBCryptPasswordEncoder(int strength, SecureRandom random) {
		super(strength, random);
	}

	public ApiBCryptPasswordEncoder(BCryptVersion version, int strength) {
		super(version, strength);
	}

	public ApiBCryptPasswordEncoder(BCryptVersion version, int strength, SecureRandom random) {
		super(version, strength, random);
	}
}
