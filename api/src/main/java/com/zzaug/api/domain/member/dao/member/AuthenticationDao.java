package com.zzaug.api.domain.member.dao.member;

import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.data.entity.member.CertificationData;
import java.util.Optional;

public interface AuthenticationDao {

	boolean existsByCertificationAndDeletedFalse(CertificationData certification);

	Optional<AuthenticationEntity> findByMemberIdAndDeletedFalse(Long memberId);

	Optional<AuthenticationEntity> findByCertificationAndDeletedFalse(
			CertificationData certification);

	AuthenticationEntity saveAuthentication(AuthenticationEntity authenticationEntity);
}
