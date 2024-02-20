package com.zzaug.api.domain.exception;

public class PasswordNotMatchException extends RuntimeException {

	public PasswordNotMatchException() {
		super("Password is not matched.");
	}
}
