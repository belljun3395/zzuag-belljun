package com.zzaug.api.domain.member.external.security.token;

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

	/**
	 * 멤버 ID를 이용하여 RoleUserAuthToken을 생성한다.
	 *
	 * @param memberId 멤버 ID
	 * @return RoleUserAuthToken
	 */
	public RoleUserAuthToken generateToken(Long memberId) {
		AuthToken authToken = tokenGenerator.generateAuthToken(memberId, List.of(Roles.ROLE_USER));
		return RoleUserAuthToken.builder()
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}
}
