package com.zzaug.api.domain.member.eventlistener;

import com.zzaug.api.domain.member.event.MemberEmailAuthMessage;
import com.zzaug.api.domain.member.external.rabbit.MemberEmailAuthMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberEmailAuthEventListener {

	private final MemberEmailAuthMessageSender sender;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
	public void onMessage(MemberEmailAuthMessage message) {
		sender.execute(message);
	}
}
