package com.zzaug.api.domain.member.model.auth;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TryCountElement {

	private static final int NEW_STATE_TRY_COUNT = 0;
	private static final int MAX_TRY_COUNT = 3;

	@Builder.Default private int tryCount = NEW_STATE_TRY_COUNT;
	@Nullable private Long emailAuthLogId;

	public static TryCountElement newState() {
		return TryCountElement.builder().build();
	}

	/**
	 * 처음 생성된 객체인지 확인합니다.
	 *
	 * @return 처음 생성된 객체인지 여부
	 */
	public boolean isNew() {
		if (!Objects.isNull(emailAuthLogId)) {
			return false;
		}
		return true;
	}

	/**
	 * 시도 횟수가 최대 시도 횟수를 초과했는지 확인합니다.
	 *
	 * @return 시도 횟수가 최대 시도 횟수를 초과했는지 여부
	 */
	public boolean isOver() {
		return tryCount >= MAX_TRY_COUNT;
	}

	/** 시도 횟수를 증가시킵니다. */
	public void plus() {
		if (isNew()) {
			if (isTryCountNewState()) {
				throw new IllegalStateException("tryCount must be 0 when state is new");
			}
			doPlus();
			return;
		}
		doPlus();
	}

	private void doPlus() {
		this.tryCount = this.tryCount + 1;
	}

	private boolean isTryCountNewState() {
		return !Objects.equals(tryCount, NEW_STATE_TRY_COUNT);
	}
}
