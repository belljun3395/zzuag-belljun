package com.zzaug.api.domain.external.dao.member;

import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.CertificationData;
import java.util.Optional;

public interface AuthenticationDao {

	boolean existsByCertificationAndDeletedFalse(CertificationData certification);

	Optional<AuthenticationEntity> findByMemberIdAndDeletedFalse(Long memberId);

	Optional<AuthenticationEntity> findByCertificationAndDeletedFalse(
			CertificationData certification);

	AuthenticationEntity saveAuthentication(AuthenticationEntity authenticationEntity);
}
