package com.zzaug.api.domain.external.service.member;

import com.zzaug.api.domain.model.member.GetMemberId;
import com.zzaug.api.domain.model.member.MemberContacts;

public interface MemberContactsQuery {

	MemberContacts execute(GetMemberId memberId);
}
