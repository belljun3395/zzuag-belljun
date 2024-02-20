package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.dto.member.LoginUseCaseRequest;
import com.zzaug.api.domain.dto.member.MemberAuthToken;
import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.CertificationData;
import com.zzaug.api.domain.entity.member.PasswordData;
import com.zzaug.api.domain.event.member.LoginEvent;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.PasswordNotMatchException;
import com.zzaug.api.domain.exception.SourceNotFoundException;
import com.zzaug.api.domain.external.dao.member.AuthenticationDao;
import com.zzaug.api.domain.external.security.auth.EnrollBlackTokenCacheCommand;
import com.zzaug.api.domain.external.service.log.LoginLogCommand;
import com.zzaug.api.domain.model.member.MemberAuthentication;
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
public class LoginUseCase {

	private final AuthenticationDao authenticationDao;

	private final LoginLogCommand loginLogCommand;

	private final ApplicationEventPublisher applicationEventPublisher;

	// security
	private final PasswordEncoder passwordEncoder;
	private final EnrollBlackTokenCacheCommand enrollBlackTokenCacheCommand;

	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public MemberAuthToken execute(LoginUseCaseRequest request) {
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		final PasswordData password = PasswordData.builder().password(request.getPassword()).build();
		final String userAgent = request.getUserAgent();

		log.debug("Get member authentication. certification: {}", certification.getCertification());
		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByCertificationAndDeletedFalse(certification);
		if (authenticationSource.isEmpty()) {
			throw new SourceNotFoundException(
					DBSource.AUTHENTICATION, "Certification", certification.getCertification());
		}
		MemberAuthentication memberAuthentication =
				MemberAuthenticationConverter.from(authenticationSource.get());

		if (!memberAuthentication.isMatchPassword(passwordEncoder, password.getPassword())) {
			throw new PasswordNotMatchException();
		}

		RoleUserAuthToken authToken =
				roleUserAuthTokenGenerator.generateToken(memberAuthentication.getMemberId());

		log.debug("Save login log. memberId: {}", memberAuthentication.getMemberId());
		loginLogCommand.saveLoginLog(memberAuthentication.getMemberId(), userAgent);

		// check duplication
		enrollBlackTokenCacheCommand.execute(authToken.getAccessToken());

		publishEvent(memberAuthentication);

		return MemberAuthToken.builder()
				.memberId(memberAuthentication.getMemberId())
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}

	private void publishEvent(MemberAuthentication memberAuthentication) {
		log.debug("Publish login event. memberId: {}", memberAuthentication.getMemberId());
		applicationEventPublisher.publishEvent(
				LoginEvent.builder().memberId(memberAuthentication.getMemberId()).build());
	}
}
