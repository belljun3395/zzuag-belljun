package com.zzaug.api.domain.support.security;

import com.zzaug.api.security.token.AuthToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RoleUserAuthToken extends AuthToken {}
