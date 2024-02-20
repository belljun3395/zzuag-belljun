package com.zzaug.api.domain.eventlistener.member;

import com.zzaug.api.domain.event.member.MemberEmailAuthMessage;
import com.zzaug.rabbitmq.config.ZRMQProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEmailAuthEventListener {

	private final RabbitTemplate rabbitTemplate;

	@EventListener
	public void onMessage(MemberEmailAuthMessage message) {
		rabbitTemplate.convertAndSend(
				ZRMQProperties.NOTIFICATION_TOPIC_NAME,
				ZRMQProperties.NOTIFICATION_KEY_NAME + ".email",
				message);
	}
}
