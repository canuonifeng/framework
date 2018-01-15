package com.codeages.framework;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableAutoConfiguration
@EnableJpaAuditing
@ComponentScan(basePackages = { "com.codeages" })
public class FrameworkApplication {
	
}