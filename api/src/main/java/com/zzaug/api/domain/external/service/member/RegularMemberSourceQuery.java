package com.zzaug.api.domain.external.service.member;

import com.zzaug.api.common.persistence.support.transaction.UseCaseTransactional;
import com.zzaug.api.domain.entity.member.MemberEntity;
import com.zzaug.api.domain.entity.member.MemberStatus;
import com.zzaug.api.domain.exception.DBSource;
import com.zzaug.api.domain.exception.SourceNotFoundException;
import com.zzaug.api.domain.external.dao.member.MemberSourceDao;
import com.zzaug.api.domain.model.member.MemberSource;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!usecase-test")
@Slf4j
@Service
@RequiredArgsConstructor
public class RegularMemberSourceQuery implements MemberSourceQuery {

	private final MemberSourceDao memberRepository;

	@UseCaseTransactional
	public MemberSource execute(Long memberId) {
		Optional<MemberEntity> entity =
				memberRepository.findByIdAndStatusAndDeletedFalse(memberId, MemberStatus.REGULAR);
		if (entity.isEmpty()) {
			log.warn("Member not found. memberId: {}", memberId);
			throw new SourceNotFoundException(DBSource.MEMBER, memberId);
		}
		MemberEntity source = entity.get();
		return new MemberSource(source.getId());
	}
}
