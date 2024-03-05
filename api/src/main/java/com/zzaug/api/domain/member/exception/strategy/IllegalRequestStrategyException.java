package com.zzaug.api.domain.member.exception.strategy;

/**
 * 요청 전략이 잘못된 경우
 *
 * <p>발생하는 예외 해당 예외의 경우 그 책임이 사용자가 아닌 개발자에게 있음
 */
public abstract class IllegalRequestStrategyException extends IllegalArgumentException {

	public IllegalRequestStrategyException() {}

	public IllegalRequestStrategyException(String s) {
		super(s);
	}

	public IllegalRequestStrategyException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalRequestStrategyException(Throwable cause) {
		super(cause);
	}
}
