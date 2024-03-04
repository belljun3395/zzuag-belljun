package com.zzaug.api.domain.member.exception.strategy;

public class IllegalNonceRequestStrategyException extends IllegalRequestStrategyException {

	public IllegalNonceRequestStrategyException() {
		super("Illegal Nonce Request Strategy");
	}
}
