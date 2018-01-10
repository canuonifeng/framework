package com.codeages.framework.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

public class BaseService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext applicationContext;
	
	protected void publish(ApplicationEvent event) {
		applicationContext.publishEvent(event);
	}
}
