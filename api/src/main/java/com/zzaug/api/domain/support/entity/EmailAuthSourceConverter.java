package com.zzaug.api.domain.support.entity;

import com.zzaug.api.domain.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.model.auth.EmailAuthSource;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailAuthSourceConverter {

	public static EmailAuthSource from(EmailAuthEntity entity) {
		return EmailAuthSource.builder()
				.id(entity.getId())
				.memberId(entity.getMemberId())
				.email(entity.getEmail().getEmail())
				.nonce(entity.getNonce())
				.code(entity.getCode())
				.build();
	}
}
