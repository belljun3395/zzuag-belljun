package com.zzaug.api.domain.external.security.auth;

import com.zzaug.api.security.entity.auth.BlackTokenAuthEntity;
import com.zzaug.api.security.entity.auth.TokenData;
import com.zzaug.api.security.entity.auth.TokenType;
import com.zzaug.api.security.persistence.auth.BlackTokenAuthRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Profile("!usecase-test")
@Component
@RequiredArgsConstructor
public class BlackTokenAuthsCommandImpl implements BlackTokenAuthsCommand {

	private final BlackTokenAuthRepository blackTokenAuthRepository;

	@Override
	@Transactional
	public void execute(String accessToken, String refreshToken) {
		blackTokenAuthRepository.saveAll(
				List.of(
						BlackTokenAuthEntity.builder()
								.token(TokenData.builder().token(accessToken).build())
								.tokenType(TokenType.ACCESSTOKEN)
								.build(),
						BlackTokenAuthEntity.builder()
								.token(TokenData.builder().token(refreshToken).build())
								.tokenType(TokenType.REFRESHTOKEN)
								.build()));
	}
}
