package com.zzaug.api.domain.exception;

public class OverMaxTryCountException extends RuntimeException {

	public OverMaxTryCountException(String message) {
		super(message);
	}
}
