package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.member.AuthenticationDao;
import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.dto.MemberAuthToken;
import com.zzaug.api.domain.member.dto.RenewalTokenUseCaseRequest;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthToken;
import com.zzaug.api.domain.member.external.security.token.RoleUserAuthTokenGenerator;
import com.zzaug.api.domain.member.model.member.MemberSource;
import com.zzaug.api.domain.member.service.member.GetMemberSourceQuery;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RenewalTokenUseCase {

	private final AuthenticationDao authenticationDao;

	private final GetMemberSourceQuery getMemberSourceQuery;

	// security
	private final RoleUserAuthTokenGenerator roleUserAuthTokenGenerator;

	@Transactional
	public MemberAuthToken execute(RenewalTokenUseCaseRequest request) {
		final Long memberId = request.getMemberId();

		MemberSource memberSource = getMemberSourceQuery.execute(memberId);

		Optional<AuthenticationEntity> authenticationSource =
				authenticationDao.findByMemberIdAndDeletedFalse(memberId);
		if (authenticationSource.isEmpty()) {
			// todo refactor: 서버, 프론트간 비정상적인 요청 예외로 처리
			throw new IllegalArgumentException();
		}

		RoleUserAuthToken authToken = roleUserAuthTokenGenerator.generateToken(memberSource.getId());

		return MemberAuthToken.builder()
				.memberId(memberSource.getId())
				.accessToken(authToken.getAccessToken())
				.refreshToken(authToken.getRefreshToken())
				.build();
	}
}
