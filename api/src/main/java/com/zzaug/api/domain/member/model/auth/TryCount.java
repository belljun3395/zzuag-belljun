package com.zzaug.api.domain.member.model.auth;

public interface TryCount {

	/**
	 * 처음 생성된 객체인지 확인합니다.
	 *
	 * @return 처음 생성된 객체인지 여부
	 */
	boolean isNew();

	/**
	 * 시도 횟수가 최대 시도 횟수를 초과했는지 확인합니다.
	 *
	 * @return 시도 횟수가 최대 시도 횟수를 초과했는지 여부
	 */
	boolean isOver();

	/** 시도 횟수를 증가시킵니다. */
	void plus();

	int getTryCount();
}
