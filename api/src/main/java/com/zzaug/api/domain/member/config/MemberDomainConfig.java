package com.zzaug.api.domain.member.config;

import com.zzaug.api.domain.member.config.encoder.ApiBCryptPasswordEncoder;
import com.zzaug.api.domain.member.config.encoder.ApiPasswordEncoder;
import com.zzaug.api.domain.member.util.usecase.RandomAuthCodeGenerator;
import com.zzaug.api.domain.member.util.usecase.RandomAuthCodeGeneratorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Member 도메인 설정 */
@Configuration
public class MemberDomainConfig {
	@Profile("!integration-test")
	@Bean
	public ApiPasswordEncoder passwordEncoder() {
		return new ApiBCryptPasswordEncoder();
	}

	@Profile("!integration-test")
	@Bean
	public RandomAuthCodeGenerator randomAuthCodeGenerator() {
		return new RandomAuthCodeGeneratorImpl();
	}
}
