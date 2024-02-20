package com.zzaug.api.domain.usecase.config.mock.security;

import com.zzaug.api.domain.external.security.session.SecuritySessionService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Profile;

@Profile("usecase-test")
@TestComponent
@RequiredArgsConstructor
public class UMockSecuritySessionService implements SecuritySessionService {

	@Override
	public Set<Object> getActiveSessionKeys() {
		Set<Object> objects = new HashSet<>();
		for (int i = 0; i < 10; i++) {
			objects.add(i);
		}
		return objects;
	}

	@Override
	public Long getMemberIdBySessionKey(Object sessionKey) {
		return (Long) sessionKey;
	}
}
