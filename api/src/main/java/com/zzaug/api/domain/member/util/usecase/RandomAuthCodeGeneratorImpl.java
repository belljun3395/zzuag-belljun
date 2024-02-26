package com.zzaug.api.domain.member.util.usecase;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomAuthCodeGeneratorImpl implements RandomAuthCodeGenerator {

	@Override
	public String generate(int count) {
		return RandomStringUtils.random(count, true, true);
	}
}
