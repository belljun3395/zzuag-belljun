package com.zzaug.api.domain.member.service.member;

import com.zzaug.api.domain.member.dao.member.MemberSourceDao;
import com.zzaug.api.domain.member.data.entity.member.MemberEntity;
import com.zzaug.api.domain.member.data.entity.member.MemberStatus;
import com.zzaug.api.domain.member.exception.argument.NotMatchMemberException;
import com.zzaug.api.domain.member.model.member.MemberSource;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile("!usecase-test")
@Slf4j
@Service
@RequiredArgsConstructor
public class GetRegularMemberSourceQuery implements GetMemberSourceQuery {

	private final MemberSourceDao memberRepository;

	@Transactional
	public MemberSource execute(Long memberId) {
		Optional<MemberEntity> entity =
				memberRepository.findByIdAndStatusAndDeletedFalse(memberId, MemberStatus.REGULAR);
		if (entity.isEmpty()) {
			throw new NotMatchMemberException();
		}
		MemberEntity source = entity.get();
		return new MemberSource(source.getId());
	}
}
