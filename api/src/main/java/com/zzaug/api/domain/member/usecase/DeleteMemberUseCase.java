package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.member.MemberSourceDao;
import com.zzaug.api.domain.member.data.entity.member.MemberEntity;
import com.zzaug.api.domain.member.data.entity.member.MemberStatus;
import com.zzaug.api.domain.member.dto.DeleteMemberUseCaseRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMemberUseCase {

	private final MemberSourceDao memberSourceDao;

	// todo 비동기로 처리 할 수 있도록 변경
	@Transactional
	public void execute(DeleteMemberUseCaseRequest request) {
		final Long memberId = request.getMemberId();

		Optional<MemberEntity> memberSource =
				memberSourceDao.findByIdAndStatusAndDeletedFalse(memberId, MemberStatus.REGULAR);
		if (memberSource.isEmpty()) {
			throw new IllegalArgumentException();
		}

		MemberEntity memberEntity = memberSource.get();
		MemberEntity withdrawnMember = memberEntity.toBuilder().status(MemberStatus.WITHDRAWN).build();
		memberSourceDao.saveSource(withdrawnMember);
	}
}
