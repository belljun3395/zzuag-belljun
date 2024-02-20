package com.zzaug.api.web.dto.member;

import com.zzaug.api.web.dto.validator.Certification;
import com.zzaug.api.web.dto.validator.Password;
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
public class LoginRequest {

	@Certification private String certification;
	@Password private String password;
}
