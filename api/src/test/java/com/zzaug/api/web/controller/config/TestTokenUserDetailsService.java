package com.zzaug.api.web.controller.config;

import com.zzaug.api.security.authentication.authority.Roles;
import com.zzaug.api.security.authentication.token.TokenUserDetails;
import java.util.List;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@TestComponent
public class TestTokenUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return TokenUserDetails.builder()
				.id("1")
				.authorities(List.of(Roles.ROLE_USER.getAuthority()))
				.build();
	}
}
