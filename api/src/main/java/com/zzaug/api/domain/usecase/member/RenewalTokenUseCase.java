package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.MemberAuthToken;
import com.zzaug.api.domain.dto.member.RenewalTokenUseCaseRequest;
import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.SourceNotFoundException;
import com.zzaug.api.domain.external.dao.member.AuthenticationDao;
import com.zzaug.api.domain.external.security.auth.BlackTokenAuthsCommand;
import com.zzaug.api.domain.external.security.auth.EnrollBlackTokenCacheCommand;
import com.zzaug.api.domain.external.security.auth.ReplaceTokenCacheService;
import com.zzaug.api.domain.external.service.member.MemberSourceQuery;
import com.zzaug.api.domain.model.member.MemberAuthentication;
import com.zzaug.api.domain.model.member.MemberSource;
import com.zzaug.api.domain.support.entity.MemberAuthenticationConverter;
import com.zzaug.api.domain.support.security.RoleUserAuthToken;
import com.zzaug.api.domain.support.security.RoleUserAuthTokenGenerator;
import com.zzaug.api.security.token.TokenResolver;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RenewalTokenUseCase {

	private final AuthenticationDao authenticationDao;

	private final MemberSourceQuery memberSourceQuery;

	// security
	private final TokenResolver tokenResolver;
	private final BlackTokenAuthsCommand blackTokenAuthCommand;
	private final EnrollBlackTokenCacheCommand enrollBlackTokenCacheCommand;
	private final ReplaceTokenCacheService replaceTokenCacheService;

	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public MemberAuthToken execute(RenewalTokenUseCaseRequest request) {
		final String refreshToken = request.getRefreshToken();
		final String accessToken = request.getAccessToken();
		final Long memberId = resolveMemberId(refreshToken);

		log.debug("Get member source. memberId: {}", memberId);
		MemberSource memberSource = memberSourceQuery.execute(memberId);

		log.debug("Get member authentication. memberId: {}", memberId);
		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByMemberIdAndDeletedFalse(memberId);
		if (authenticationSource.isEmpty()) {
			throw new SourceNotFoundException(DBSource.AUTHENTICATION, "MemberId", memberId);
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		RoleUserAuthToken authToken = roleUserAuthTokenGenerator.generateToken(memberSource.getId());

		blackTokenAuthCommand.execute(accessToken, refreshToken);
		enrollBlackTokenCacheCommand.execute(accessToken, refreshToken);
		replaceTokenCacheService.execute(
				accessToken, authToken.getAccessToken(), memberAuthentication.getMemberId());

		return MemberAuthToken.builder()
				.memberId(memberSource.getId())
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}

	@NotNull
	private Long resolveMemberId(String refreshToken) {
		Optional<Long> idSource = tokenResolver.resolveId(refreshToken);
		if (idSource.isEmpty()) {
			throw new IllegalStateException("MemberId is not found in refreshToken.");
		}
		return idSource.get();
	}
}
