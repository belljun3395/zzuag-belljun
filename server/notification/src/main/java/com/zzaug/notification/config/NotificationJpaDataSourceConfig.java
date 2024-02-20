package com.zzaug.notification.config;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = NotificationAppConfig.BASE_PACKAGE,
		transactionManagerRef = NotificationJpaDataSourceConfig.TRANSACTION_MANAGER_NAME,
		entityManagerFactoryRef = NotificationJpaDataSourceConfig.NOTIFICATION_MANAGER_FACTORY_NAME)
public class NotificationJpaDataSourceConfig {

	public static final String NOTIFICATION_BEAN_NAME_PREFIX = NotificationAppConfig.BEAN_NAME_PREFIX;
	public static final String NOTIFICATION_MANAGER_FACTORY_NAME =
			NOTIFICATION_BEAN_NAME_PREFIX + "ManagerFactory";
	public static final String TRANSACTION_MANAGER_NAME =
			NOTIFICATION_BEAN_NAME_PREFIX + "TransactionManager";
	public static final String DATASOURCE_NAME = NOTIFICATION_BEAN_NAME_PREFIX + "DataSource";
	private static final String JPA_PROPERTIES_NAME = NOTIFICATION_BEAN_NAME_PREFIX + "JpaProperties";
	private static final String HIBERNATE_PROPERTIES_NAME =
			NOTIFICATION_BEAN_NAME_PREFIX + "HibernateProperties";
	private static final String JPA_VENDOR_ADAPTER_NAME =
			NOTIFICATION_BEAN_NAME_PREFIX + "JpaVendorAdapter";
	private static final String PERSIST_UNIT = NOTIFICATION_BEAN_NAME_PREFIX + "PersistenceUnit";
	private static final String NOTIFICATION_MANAGER_FACTORY_BUILDER_NAME =
			NOTIFICATION_BEAN_NAME_PREFIX + "ManagerFactoryBuilder";

	@Bean(name = DATASOURCE_NAME)
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = JPA_PROPERTIES_NAME)
	@ConfigurationProperties(prefix = "spring.jpa")
	public JpaProperties jpaProperties() {
		return new JpaProperties();
	}

	@Bean(name = HIBERNATE_PROPERTIES_NAME)
	@ConfigurationProperties(prefix = "spring.jpa.hibernate")
	public HibernateProperties hibernateProperties() {
		return new HibernateProperties();
	}

	@Bean(name = JPA_VENDOR_ADAPTER_NAME)
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean(name = NOTIFICATION_MANAGER_FACTORY_BUILDER_NAME)
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
			@Qualifier(value = JPA_VENDOR_ADAPTER_NAME) JpaVendorAdapter jpaVendorAdapter,
			@Qualifier(value = JPA_PROPERTIES_NAME) JpaProperties jpaProperties,
			ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {

		Map<String, String> jpaPropertyMap = jpaProperties.getProperties();
		return new EntityManagerFactoryBuilder(
				jpaVendorAdapter, jpaPropertyMap, persistenceUnitManager.getIfAvailable());
	}

	@Bean(name = NOTIFICATION_MANAGER_FACTORY_NAME)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource,
			@Qualifier(value = NOTIFICATION_MANAGER_FACTORY_BUILDER_NAME)
					EntityManagerFactoryBuilder builder) {
		Map<String, String> jpaPropertyMap = jpaProperties().getProperties();
		Map<String, Object> hibernatePropertyMap =
				hibernateProperties().determineHibernateProperties(jpaPropertyMap, new HibernateSettings());
		return builder
				.dataSource(dataSource)
				.properties(hibernatePropertyMap)
				.persistenceUnit(PERSIST_UNIT)
				.packages(NotificationAppConfig.BASE_PACKAGE)
				.build();
	}

	@Bean(name = TRANSACTION_MANAGER_NAME)
	public PlatformTransactionManager transactionManager(
			@Qualifier(NOTIFICATION_MANAGER_FACTORY_NAME) EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}