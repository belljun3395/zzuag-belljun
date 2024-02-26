package com.zzaug.api.domain.member.service.member;

import com.zzaug.api.domain.member.model.member.MemberSource;

public interface GetMemberSourceQuery {

	MemberSource execute(Long memberId);
}
