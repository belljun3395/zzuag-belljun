package com.zzaug.api.domain.member.usecase.config.mock.service;

import com.zzaug.api.domain.member.model.member.MemberSource;
import com.zzaug.api.domain.member.service.member.GetMemberSourceQuery;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Profile;

/**
 * 테스트용 멤버 소스 쿼리
 *
 * <p>요청한 멤버 ID에 대한 멤버 소스를 반환합니다.
 */
@Profile("usecase-test")
@TestComponent
public class UMockGetMemberSourceQuery implements GetMemberSourceQuery {

	@Override
	public MemberSource execute(Long memberId) {
		return MemberSource.builder().id(memberId).build();
	}
}
