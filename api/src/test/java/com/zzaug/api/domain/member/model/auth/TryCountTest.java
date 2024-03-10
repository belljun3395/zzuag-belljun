package com.zzaug.api.domain.member.model.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TryCountTest {

	@Test
	void 처음_생성된_객체인지_확인합니다() {
		// given
		TryCount newState = NewTryCountElement.newState();

		// when
		boolean result = newState.isNew();

		// then
		assertTrue(result);
	}

	@Test
	void 시도_횟수가_최대_시도_횟수를_초과했는지_확인합니다() {
		// given
		int maxTryCount = 3;
		TryCount savedTryCountElement = SavedTryCountElement.builder().tryCount(maxTryCount).build();

		// when
		boolean result = savedTryCountElement.isOver();

		// then
		assertTrue(result);
	}

	@Test
	void 시도_횟수를_증가시킵니다() {
		// given
		int givenTryCount = 0;
		TryCount savedTryCountElement = SavedTryCountElement.builder().tryCount(givenTryCount).build();

		// when
		savedTryCountElement.plus();

		// then
		assertEquals(givenTryCount + 1, savedTryCountElement.getTryCount());
	}

	@Test
	void 새로운_상태에서_시도_횟수를_여러번_증가시키면_예외가_발생합니다() {
		// given
		TryCount newState = NewTryCountElement.newState();

		// when
		newState.plus();

		// then
		assertThrows(IllegalStateException.class, newState::plus);
	}
}
