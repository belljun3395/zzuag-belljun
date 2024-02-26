package com.zzaug.api.domain.common.persistence.support.checker;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class RepositoryMethodChecker implements ApplicationContextAware, ApplicationRunner {

	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Map<String, Object> repositories = context.getBeansWithAnnotation(ZzuagRepository.class);
		for (Object repository : repositories.values()) {
			Method[] methods = repository.getClass().getDeclaredMethods();
			for (Method method : methods) {
				validateMethod(method);
			}
		}
	}

	private static void validateMethod(Method method) {
		validateDeleteFalse(method);
	}

	private static void validateDeleteFalse(Method method) {
		DeletedFalse annotation = AnnotationUtils.findAnnotation(method, DeletedFalse.class);
		String name = method.getName().toLowerCase();
		if (Objects.isNull(annotation)) {
			if (!name.contains("deletedfalse")) {
				return;
			}
			throw new RepositoryQueryMethodAnnotationMissingException(method);
		}
		String value = annotation.value().toLowerCase();
		if (!name.contains(value)) {
			throw new RepositoryQueryMissingException(method);
		}
	}
}
