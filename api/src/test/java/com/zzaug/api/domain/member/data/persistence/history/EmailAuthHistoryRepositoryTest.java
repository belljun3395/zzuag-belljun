package com.zzaug.api.domain.member.data.persistence.history;

import static org.junit.jupiter.api.Assertions.*;

import com.zzaug.api.domain.member.data.entity.history.EmailAuthHistoryEntity;
import com.zzaug.api.domain.member.data.persistence.AbstractRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmailAuthHistoryRepositoryTest extends AbstractRepositoryTest {
	@Autowired EmailAuthHistoryRepository repository;

	@BeforeEach
	void setUp() {
		repository.deleteAll();
		EmailAuthHistoryEntity successEntity =
				EmailAuthHistoryEntity.builder().memberId(1L).emailAuthId(1L).reason("SUCCESS").build();
		repository.save(successEntity);
		EmailAuthHistoryEntity notMatchEntity =
				EmailAuthHistoryEntity.builder()
						.memberId(1L)
						.emailAuthId(1L)
						.reason("NOT_MATCH_CODE")
						.build();
		repository.save(notMatchEntity);
	}

	@Test
	void 멤버_아이디와_이메일_인증_아이디_그리고_성공한_내역을_조회한다() {
		// given
		Long memberId = 1L;
		Long emailAuthId = 1L;
		String reason = "SUCCESS";

		// when
		var result =
				repository.findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
						memberId, emailAuthId, reason);

		// then
		assertTrue(result.isPresent());
	}

	@Test
	void 멤버_아이디와_이메일_인증_아이디_그리고_성공하지_않은_내역을_조회한다() {
		// given
		Long memberId = 1L;
		Long emailAuthId = 1L;
		String reason = "NOT_MATCH_CODE";

		// when
		var result =
				repository.findByMemberIdAndEmailAuthIdAndReasonNotAndDeletedFalse(
						memberId, emailAuthId, reason);

		// then
		assertTrue(result.isPresent());
	}
}
