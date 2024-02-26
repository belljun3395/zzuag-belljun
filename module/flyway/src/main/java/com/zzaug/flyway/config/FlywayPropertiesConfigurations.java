package com.zzaug.flyway.config;

import static com.zzaug.flyway.FlywayConfig.BEAN_NAME_PREFIX;
import static com.zzaug.flyway.FlywayConfig.PROPERTY_PREFIX;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class})
public class FlywayPropertiesConfigurations {

	private static final String BASE_BEAN_NAME_PREFIX = BEAN_NAME_PREFIX;

	@Profile("api")
	@Bean(name = BASE_BEAN_NAME_PREFIX + "apiProperties")
	@ConfigurationProperties(prefix = PROPERTY_PREFIX + ".api")
	public FlywayProperties flywayApiProperties() {
		return new FlywayProperties();
	}

	@Profile("notification")
	@Bean(name = BASE_BEAN_NAME_PREFIX + "notificationProperties")
	@ConfigurationProperties(prefix = PROPERTY_PREFIX + ".notification")
	public FlywayProperties flywayNotificationProperties() {
		return new FlywayProperties();
	}
}
