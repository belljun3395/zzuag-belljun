package com.zzaug.api.security.eventListener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.zzaug.api.security.entity.log.InvalidTokenAccessEntity;
import com.zzaug.api.security.event.InvalidTokenAccessEvent;
import com.zzaug.api.security.persistence.log.InvalidTokenAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InvalidTokenAccessEventListener {

	private final InvalidTokenAccessRepository invalidTokenAccessRepository;

	@TransactionalEventListener
	@Transactional(propagation = REQUIRES_NEW)
	public void onMessage(InvalidTokenAccessEvent message) {
		invalidTokenAccessRepository.save(
				InvalidTokenAccessEntity.builder()
						.token(message.getToken())
						.ip(message.getIp())
						.userAgent(message.getUserAgent())
						.build());
	}
}
