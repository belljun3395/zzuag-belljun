package com.zzaug.api.domain.common.persistence.support.checker;

import java.lang.reflect.Method;

public class RepositoryQueryMissingException extends RuntimeException {

	public RepositoryQueryMissingException(Method method) {
		super(method.getName() + " have annotation but not have query");
	}
}
