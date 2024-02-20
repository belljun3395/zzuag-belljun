package com.zzaug.api.domain.support.security;

import com.zzaug.api.security.authentication.authority.Roles;
import com.zzaug.api.security.token.AuthToken;
import com.zzaug.api.security.token.TokenGenerator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleUserAuthTokenGenerator {

	private final TokenGenerator tokenGenerator;

	public RoleUserAuthToken generateToken(Long memberId) {
		AuthToken authToken = tokenGenerator.generateAuthToken(memberId, List.of(Roles.ROLE_USER));
		return RoleUserAuthToken.builder()
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}
}
