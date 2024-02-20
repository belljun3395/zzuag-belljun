package com.zzaug.api.domain.external.security.session;

import static com.zzaug.api.config.ApiSessionConfig.SESSION_NAME_SPACE;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!usecase-test")
@Component
@RequiredArgsConstructor
public class SecuritySessionServiceImpl implements SecuritySessionService {

	private static final String MEMBER_ID_SESSION_KEY = "sessionAttr:memberId";

	private final RedisIndexedSessionRepository redisIndexedSessionRepository;

	@Override
	public Set<Object> getActiveSessionKeys() {
		Set<Object> keys =
				redisIndexedSessionRepository
						.getSessionRedisOperations()
						.opsForList()
						.getOperations()
						.keys(SESSION_NAME_SPACE + ":sessions:*");
		assert keys != null;
		return keys.stream()
				.filter(key -> !key.toString().contains("expires"))
				.collect(Collectors.toSet());
	}

	@Override
	public Long getMemberIdBySessionKey(Object sessionKey) {
		return (Long)
				redisIndexedSessionRepository
						.getSessionRedisOperations()
						.opsForHash()
						.get(sessionKey, MEMBER_ID_SESSION_KEY);
	}
}
