package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.UpdateMemberUseCaseRequest;
import com.zzaug.api.domain.dto.member.UpdateMemberUseCaseResponse;
import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.CertificationData;
import com.zzaug.api.domain.event.member.UpdateMemberEvent;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.ExistSourceException;
import com.zzaug.api.domain.exception.PasswordNotMatchException;
import com.zzaug.api.domain.exception.SourceNotFoundException;
import com.zzaug.api.domain.external.dao.member.AuthenticationDao;
import com.zzaug.api.domain.external.security.AuthTokenValidator;
import com.zzaug.api.domain.external.security.auth.BlackTokenAuthsCommand;
import com.zzaug.api.domain.external.security.auth.EnrollBlackTokenCacheCommand;
import com.zzaug.api.domain.external.security.auth.ReplaceTokenCacheService;
import com.zzaug.api.domain.external.service.member.MemberSourceQuery;
import com.zzaug.api.domain.model.member.MemberAuthentication;
import com.zzaug.api.domain.model.member.MemberSource;
import com.zzaug.api.domain.support.entity.MemberAuthenticationConverter;
import com.zzaug.api.domain.support.security.RoleUserAuthToken;
import com.zzaug.api.domain.support.security.RoleUserAuthTokenGenerator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateMemberUseCase {

	private final AuthenticationDao authenticationDao;

	private final MemberSourceQuery memberSourceQuery;

	private final ApplicationEventPublisher applicationEventPublisher;

	// security
	private final PasswordEncoder passwordEncoder;
	private final AuthTokenValidator authTokenValidator;
	private final BlackTokenAuthsCommand blackTokenAuthCommand;
	private final EnrollBlackTokenCacheCommand enrollBlackTokenCacheCommand;
	private final ReplaceTokenCacheService replaceTokenCacheService;

	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public UpdateMemberUseCaseResponse execute(UpdateMemberUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		final String password = request.getPassword();
		final String accessToken = request.getAccessToken();
		final String refreshToken = request.getRefreshToken();

		authTokenValidator.execute(refreshToken, accessToken, memberId);

		MemberSource memberSource = memberSourceQuery.execute(memberId);

		log.debug("Get authentication. memberId: {}", memberId);
		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByMemberIdAndDeletedFalse(memberSource.getId());
		if (authenticationSource.isEmpty()) {
			throw new SourceNotFoundException(DBSource.AUTHENTICATION, "MemberId", memberId);
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		if (!memberAuthentication.isMatchPassword(passwordEncoder, password)) {
			throw new PasswordNotMatchException();
		}

		if (!memberAuthentication.isSameCertification(certification.getCertification())) {
			// todo false이면 certification을 기준으로 락을 걸어 처리 해야 함
			log.debug(
					"Check duplicate certification. certification: {}", certification.getCertification());
			boolean isDuplicateId = authenticationDao.existsByCertificationAndDeletedFalse(certification);
			if (isDuplicateId) {
				throw new ExistSourceException(DBSource.AUTHENTICATION, certification.getCertification());
			}
			String originCertificationValue = memberAuthentication.getCertification();
			memberAuthentication.updateCertification(certification.getCertification());
			log.debug(
					"Update certification. memberId: {}, originCertification: {}, newCertification: {}",
					memberId,
					originCertificationValue,
					certification.getCertification());
		}

		AuthenticationEntity editAuthentication =
				MemberAuthenticationConverter.to(memberAuthentication);
		AuthenticationEntity authenticationEntity =
				authenticationDao.saveAuthentication(editAuthentication);
		log.debug(
				"Update authentication. memberId: {}, authenticationId: {}",
				memberId,
				authenticationEntity.getId());

		RoleUserAuthToken authToken = roleUserAuthTokenGenerator.generateToken(memberSource.getId());

		blackTokenAuthCommand.execute(accessToken, refreshToken);
		enrollBlackTokenCacheCommand.execute(accessToken, refreshToken);
		replaceTokenCacheService.execute(
				accessToken, authToken.getAccessToken(), memberAuthentication.getMemberId());

		publishEvent(memberId, memberAuthentication);

		return UpdateMemberUseCaseResponse.builder()
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}

	private void publishEvent(Long memberId, MemberAuthentication memberAuthentication) {
		log.debug("Publish update member event. memberId: {}", memberId);
		applicationEventPublisher.publishEvent(
				UpdateMemberEvent.builder()
						.memberId(memberId)
						.memberCertification(memberAuthentication.getCertification())
						.build());
	}
}
