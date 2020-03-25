package com.codeages.framework;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@EnableJpaAuditing
@ComponentScan(basePackages = { "com.codeages" })
@EntityScan("com.codeages")
@EnableJpaRepositories("com.codeages")
@EnableAsync
@EnableScheduling
public class FrameworkApplication {

	
	
}