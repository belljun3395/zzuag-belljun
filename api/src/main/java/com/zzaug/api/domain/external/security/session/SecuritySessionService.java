package com.zzaug.api.domain.external.security.session;

import java.util.Set;

public interface SecuritySessionService {

	Set<Object> getActiveSessionKeys();

	Long getMemberIdBySessionKey(Object sessionKey);
}
