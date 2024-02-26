package com.zzaug.api.domain.member.external.rabbit;

import com.zzaug.api.domain.member.event.MemberEmailAuthMessage;
import com.zzaug.rabbitmq.config.ZRMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEmailAuthMessageSender {

	private final RabbitTemplate rabbitTemplate;

	public void execute(MemberEmailAuthMessage message) {
		rabbitTemplate.convertAndSend(
				ZRMQProperties.NOTIFICATION_TOPIC_NAME,
				ZRMQProperties.NOTIFICATION_KEY_NAME + ".email",
				message);
	}
}
