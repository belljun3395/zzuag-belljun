package com.zzaug.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class ApiCacheConfig {

	@Bean
	public CacheManager redisCacheManager(
			@Qualifier(ApiRedisConfig.REDIS_CONNECTION_FACTORY_NAME)
					RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration =
				RedisCacheConfiguration.defaultCacheConfig()
						.serializeKeysWith(
								RedisSerializationContext.SerializationPair.fromSerializer(
										new StringRedisSerializer()))
						.serializeValuesWith(
								RedisSerializationContext.SerializationPair.fromSerializer(
										new GenericJackson2JsonRedisSerializer()));

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration)
				.build();
	}
}
