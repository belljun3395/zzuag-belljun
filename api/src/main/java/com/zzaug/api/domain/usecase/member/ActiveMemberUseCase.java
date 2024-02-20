package com.zzaug.api.domain.usecase.member;

import com.zzaug.api.domain.external.security.session.SecuritySessionService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// todo make controller
@Slf4j
@Service
@RequiredArgsConstructor
public class ActiveMemberUseCase {

	private final SecuritySessionService securitySessionService;

	@Transactional
	public Set<Long> execute() {
		Set<Long> memberIds = new HashSet<>();
		log.debug("Get active session keys");
		Set<Object> keys = securitySessionService.getActiveSessionKeys();
		log.debug("Get member ids by session keys");
		for (Object key : keys) {
			Long memberId = securitySessionService.getMemberIdBySessionKey(key);
			memberIds.add(memberId);
		}
		return memberIds;
	}
}
