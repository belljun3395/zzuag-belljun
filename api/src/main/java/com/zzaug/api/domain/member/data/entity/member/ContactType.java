package com.zzaug.api.domain.member.data.entity.member;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ContactType {
	EMAIL("이메일"),
	GITHUB("깃허브"),
	;

	private final String description;

	ContactType(String description) {
		this.description = description;
	}
}
