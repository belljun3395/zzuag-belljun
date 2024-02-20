package com.zzaug.api.listener.service.member;

import com.zzaug.api.security.redis.auth.WhiteAuthTokenHash;
import com.zzaug.api.security.redis.auth.WhiteAuthTokenHashRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberWhiteAuthTokenEvictServiceImpl implements MemberWhiteAuthTokenEvictService {

	private final WhiteAuthTokenHashRepository whiteAuthTokenHashRepository;

	@Override
	public void execute(Long memberId) {
		List<WhiteAuthTokenHash> memberIds = whiteAuthTokenHashRepository.findAllByMemberId(memberId);
		whiteAuthTokenHashRepository.deleteAll(memberIds);
	}
}
