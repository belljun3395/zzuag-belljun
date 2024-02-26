package com.zzaug.api.config;

import com.zzaug.rabbitmq.config.ZRabbitMQUserConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {ZRabbitMQUserConfig.class})
public class ApiRabbitMQConfig {}
