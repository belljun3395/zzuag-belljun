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
	private int tryCount;
	@Nullable private Long emailAuthLogId;

	/**
	 * 처음 생성된 객체인지 확인합니다.
	 *
	 * @return 처음 생성된 객체인지 여부
	 */
	public boolean isNew() {
		return Objects.isNull(emailAuthLogId);
	}

	/** 시도 횟수를 증가시킵니다. */
	public void plus() {
		assert !isNew();
		this.tryCount = this.tryCount + 1;
	}
}
