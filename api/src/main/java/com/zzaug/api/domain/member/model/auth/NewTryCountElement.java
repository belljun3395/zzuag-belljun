package com.zzaug.api.domain.member.model.auth;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewTryCountElement implements TryCount {

	private static final int NEW_STATE_TRY_COUNT = 0;

	private int tryCount;

	public static NewTryCountElement newState() {
		return new NewTryCountElement(NEW_STATE_TRY_COUNT);
	}

	@Override
	public boolean isNew() {
		return true;
	}

	@Override
	public boolean isOver() {
		return false;
	}

	@Override
	public void plus() {
		if (isTryCountNewState()) {
			throw new IllegalStateException("tryCount must be 0 when state is new");
		}
		this.tryCount = this.tryCount + 1;
	}

	private boolean isTryCountNewState() {
		return !Objects.equals(tryCount, NEW_STATE_TRY_COUNT);
	}
}
