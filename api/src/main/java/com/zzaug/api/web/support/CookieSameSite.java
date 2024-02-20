package com.zzaug.api.web.support;

import lombok.Getter;

@Getter
public enum CookieSameSite {
	NONE("None"),
	LAX("Lax"),
	STRICT("Strict");

	private String value;

	CookieSameSite(String value) {
		this.value = value;
	}
}
