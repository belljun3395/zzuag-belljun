package com.zzaug.api.domain.member.exception.argument;

public class NotMatchPasswordException extends IllegalArgumentException {

	public NotMatchPasswordException() {
		super("Not Match Password");
	}
}
