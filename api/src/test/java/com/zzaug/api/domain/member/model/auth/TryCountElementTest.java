package com.zzaug.api.domain.member.model.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TryCountElementTest {

	@Test
	void 처음_생성된_객체인지_확인합니다() {
		// given
		TryCountElement newState = TryCountElement.newState();

		// when
		boolean result = newState.isNew();

		// then
		assertTrue(result);
	}

	@Test
	void 시도_횟수가_최대_시도_횟수를_초과했는지_확인합니다() {
		// given
		int maxTryCount = 3;
		TryCountElement tryCountElement = TryCountElement.builder().tryCount(maxTryCount).build();

		// when
		boolean result = tryCountElement.isOver();

		// then
		assertTrue(result);
	}

	@Test
	void 시도_횟수를_증가시킵니다() {
		// given
		int givenTryCount = 0;
		TryCountElement tryCountElement = TryCountElement.builder().tryCount(givenTryCount).build();

		// when
		tryCountElement.plus();

		// then
		assertEquals(givenTryCount + 1, tryCountElement.getTryCount());
	}

	@Test
	void 새로운_상태에서_시도_횟수를_여러번_증가시키면_예외가_발생합니다() {
		// given
		TryCountElement newState = TryCountElement.newState();

		// when
		newState.plus();

		// then
		assertThrows(IllegalStateException.class, newState::plus);
	}
}
