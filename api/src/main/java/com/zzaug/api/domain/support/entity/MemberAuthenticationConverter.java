package com.zzaug.api.domain.support.entity;

import com.zzaug.api.domain.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.entity.member.CertificationData;
import com.zzaug.api.domain.entity.member.PasswordData;
import com.zzaug.api.domain.model.member.MemberAuthentication;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MemberAuthenticationConverter {

	public static MemberAuthentication from(AuthenticationEntity entity) {
		return MemberAuthentication.builder()
				.id(entity.getId())
				.memberId(entity.getMemberId())
				.certification(entity.getCertification().getCertification())
				.password(entity.getPassword().getPassword())
				.build();
	}

	public static AuthenticationEntity to(MemberAuthentication source) {
		return AuthenticationEntity.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.certification(CertificationData.builder().certification(source.getCertification()).build())
				.password(PasswordData.builder().password(source.getPassword()).build())
				.build();
	}
}
