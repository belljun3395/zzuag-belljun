package com.zzaug.api.integration.member.config;

import com.zzaug.api.domain.member.config.encoder.ApiPasswordEncoder;
import com.zzaug.api.domain.member.util.usecase.RandomAuthCodeGenerator;
import com.zzaug.api.integration.member.config.encoder.IntegrationTestApiBCryptPasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MemberIntegrationTestConfig {

	@Profile("integration-test")
	public ApiPasswordEncoder passwordEncoder() {
		return new IntegrationTestApiBCryptPasswordEncoder();
	}

	@Profile("integration-test")
	public RandomAuthCodeGenerator randomAuthCodeGenerator() {
		return new IntegrationTestRandomAuthCodeGeneratorImpl();
	}
}
