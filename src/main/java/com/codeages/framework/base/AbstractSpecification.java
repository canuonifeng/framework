package com.codeages.framework.base;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

abstract public class AbstractSpecification<T> implements Specification<T>{
	
	protected Map<String, Object> conditions;

	public AbstractSpecification(Map<String, Object> conditions) {
		this.conditions = conditions;
	}
}
