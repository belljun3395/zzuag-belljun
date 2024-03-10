package com.zzaug.api.domain.member.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SavedTryCountElement implements TryCount {

	private static final int MAX_TRY_COUNT = 3;

	private int tryCount;
	private Long emailAuthLogId;

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public boolean isOver() {
		return tryCount >= MAX_TRY_COUNT;
	}

	@Override
	public void plus() {
		this.tryCount = this.tryCount + 1;
	}
}
