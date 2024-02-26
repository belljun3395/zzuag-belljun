package com.zzaug.api.domain.member.external.security.token;

import com.zzaug.api.security.token.AuthToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/** Security의 AuthToken을 상속받아 RoleUserAuthToken을 정의한다. */
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RoleUserAuthToken extends AuthToken {}
