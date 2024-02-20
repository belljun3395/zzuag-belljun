package com.zzaug.api.domain.eventlistener.member;

import com.zzaug.api.domain.event.member.AddEmailEvent;
import com.zzaug.api.domain.event.member.UpdateMemberEvent;
import com.zzaug.rabbitmq.config.ZRMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberInfoUpdatedEventListener {

	private final RabbitTemplate rabbitTemplate;

	@EventListener
	public void onMessage(AddEmailEvent message) {
		rabbitTemplate.convertAndSend(
				ZRMQProperties.MEMBER_TOPIC_NAME,
				ZRMQProperties.MEMBER_UPDATED_KEY_NAME + ".email",
				message);
	}

	@EventListener
	public void onMessage(UpdateMemberEvent message) {
		rabbitTemplate.convertAndSend(
				ZRMQProperties.MEMBER_TOPIC_NAME, ZRMQProperties.MEMBER_UPDATED_KEY_NAME, message);
	}
}
