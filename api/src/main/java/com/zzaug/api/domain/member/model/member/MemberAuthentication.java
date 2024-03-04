package com.zzaug.api.domain.member.model.member;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MemberAuthentication implements GetMemberId {

	private Long id;
	private Long memberId;
	private String certification;
	private String password; // encoded password

	/**
	 * 동일한 Certification인지 확인합니다.
	 *
	 * @return 동일한 Certification인지 여부
	 */
	public boolean isSameCertification(String certification) {
		return Objects.equals(this.certification, certification);
	}

	/**
	 * Certification을 업데이트합니다.
	 *
	 * @param certification 새로운 Certification
	 */
	public void updateCertification(String certification) {
		this.certification = certification;
	}

	/**
	 * 비밀번호가 일치하는지 확인합니다.
	 *
	 * @param passwordEncoder 비밀번호 인코더
	 * @param password 비밀번호
	 * @return 비밀번호가 일치하는지 여부
	 */
	public boolean isMatchPassword(PasswordEncoder passwordEncoder, String password) {
		return passwordEncoder.matches(password, this.password);
	}
}
