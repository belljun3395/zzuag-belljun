package com.zzaug.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = ApiAppConfig.BASE_PACKAGE)
public class ApiAppConfig {

	public static final String BASE_PACKAGE = "com.zzaug.api";
	public static final String SERVICE_NAME = "recycle";
	public static final String MODULE_NAME = "api";
	public static final String BEAN_NAME_PREFIX = "api";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
