package com.zzaug.api.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(
		basePackages = ApiAppConfig.BASE_PACKAGE,
		redisTemplateRef = ApiRedisConfig.REDIS_TEMPLATE_NAME)
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class})
public class ApiRedisConfig {
	public static final String REDIS_BEAN_NAME_PREFIX = ApiAppConfig.BEAN_NAME_PREFIX + "Redis";
	public static final String REDIS_PROPERTIES_NAME = REDIS_BEAN_NAME_PREFIX + "Properties";
	public static final String REDIS_CONNECTION_FACTORY_NAME =
			REDIS_BEAN_NAME_PREFIX + "ConnectionFactory";
	public static final String REDIS_TEMPLATE_NAME = REDIS_BEAN_NAME_PREFIX + "Template";

	@Bean(name = REDIS_PROPERTIES_NAME)
	public RedisProperties redisProperties() {
		return new RedisProperties();
	}

	@Bean(name = REDIS_CONNECTION_FACTORY_NAME)
	public RedisConnectionFactory redisConnectionFactory() {
		String redisHost = redisProperties().getHost();
		int redisPort = redisProperties().getPort();
		return new LettuceConnectionFactory(redisHost, redisPort);
	}

	@Bean(name = REDIS_TEMPLATE_NAME)
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}
