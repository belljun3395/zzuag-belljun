package com.zzaug.api.config;

import com.zzaug.flyway.FlywayConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {FlywayConfig.class})
public class ApiFlywayConfig {}
