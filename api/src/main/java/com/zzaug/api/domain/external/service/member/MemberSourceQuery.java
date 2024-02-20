package com.zzaug.api.domain.external.service.member;

import com.zzaug.api.domain.model.member.MemberSource;

public interface MemberSourceQuery {

	MemberSource execute(Long memberId);
}
