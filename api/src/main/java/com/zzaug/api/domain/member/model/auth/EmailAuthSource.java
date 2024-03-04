package com.zzaug.api.domain.member.model.auth;

import java.util.Objects;
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
public class EmailAuthSource {

	private Long id;
	private Long memberId;

	private String email;
	private String nonce;
	private String code;

	/**
	 * 이메일 인증 요청 멤버와 동일한 멤버인지 확인합니다.
	 *
	 * @param memberId 멤버 아이디
	 * @return 동일한 멤버인지 여부
	 */
	public boolean isMemberId(Long memberId) {
		return Objects.equals(this.memberId, memberId);
	}

	/**
	 * 이메일 인증 요청시 발급한 nonce와 요청한 nonce가 일치하는지 확인합니다.
	 *
	 * @param nonce 요청한 nonce
	 * @return 일치하는지 여부
	 */
	public boolean isNonce(String nonce) {
		return Objects.equals(this.nonce, nonce);
	}

	/**
	 * 이메일 인증 요청시 발급한 code와 요청한 code가 일치하는지 확인합니다.
	 *
	 * @param code 요청한 code
	 * @return 일치하는지 여부
	 */
	public boolean isCode(String code) {
		return Objects.equals(this.code, code);
	}
}
