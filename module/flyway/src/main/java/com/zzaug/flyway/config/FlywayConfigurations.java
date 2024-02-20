package com.zzaug.flyway.config;

import static com.zzaug.flyway.FlywayConfig.BEAN_NAME_PREFIX;
import static com.zzaug.flyway.FlywayConfig.PROPERTY_PREFIX;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class})
public class FlywayConfigurations {

	// base property prefix for flyway
	private static final String BASE_BEAN_NAME_PREFIX = BEAN_NAME_PREFIX;

	// bean name for flyway configuration
	private static final String FLYWAY = BASE_BEAN_NAME_PREFIX;
	private static final String FLYWAY_MIGRATION_INITIALIZER =
			BASE_BEAN_NAME_PREFIX + "MigrationInitializer";
	private static final String FLYWAY_VALIDATE_INITIALIZER =
			BASE_BEAN_NAME_PREFIX + "ValidateInitializer";
	private static final String FLYWAY_CONFIGURATION = BASE_BEAN_NAME_PREFIX + "Configuration";

	@Bean(name = FLYWAY)
	public Flyway flyway(org.flywaydb.core.api.configuration.Configuration configuration) {
		return new Flyway(configuration);
	}

	@Profile({"!local && !new"})
	@Bean(name = FLYWAY_VALIDATE_INITIALIZER)
	public FlywayMigrationInitializer flywayValidateInitializer(
			@Qualifier(value = FLYWAY) Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::validate);
	}

	@Profile({"!local"})
	@Bean(name = FLYWAY_MIGRATION_INITIALIZER)
	public FlywayMigrationInitializer flywayMigrationInitializer(
			@Qualifier(value = FLYWAY) Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::migrate);
	}

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

	@Bean(name = FLYWAY_CONFIGURATION)
	public org.flywaydb.core.api.configuration.Configuration Configuration(
			ObjectProvider<DataSource> dataSource, ObjectProvider<FlywayProperties> flywayApiProperties) {
		FluentConfiguration configuration = new FluentConfiguration();
		configuration.dataSource(dataSource.getIfAvailable());
		flywayApiProperties.getIfAvailable().getLocations().forEach(configuration::locations);
		return configuration;
	}
}
