package com.zzaug.api.listener.rabbit.member;

import com.rabbitmq.client.Channel;
import com.zzaug.api.listener.rabbit.MemberIdMapper;
import com.zzaug.api.listener.service.member.MemberWhiteAuthTokenEvictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberModifiedEventListener implements ChannelAwareMessageListener {

	private final MemberIdMapper memberIdMapper;
	private final MemberWhiteAuthTokenEvictService memberWhiteAuthTokenEvictService;

	@Override
	public void onMessage(Message message, @Nullable Channel channel) throws Exception {
		Long memberId = memberIdMapper.map(message);
		memberWhiteAuthTokenEvictService.execute(memberId);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}
}
