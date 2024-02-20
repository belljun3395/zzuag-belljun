package com.zzaug.api.config;

import com.zzaug.flyway.FlywayConfig;
import com.zzaug.rabbitmq.config.ZRabbiMQConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = ApiAppConfig.BASE_PACKAGE)
@Import(value = {FlywayConfig.class, ZRabbiMQConfig.class})
public class ApiAppConfig {

	public static final String BASE_PACKAGE = "com.zzaug.api";
	public static final String SERVICE_NAME = "recycle";
	public static final String MODULE_NAME = "api";
	public static final String BEAN_NAME_PREFIX = "api";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
