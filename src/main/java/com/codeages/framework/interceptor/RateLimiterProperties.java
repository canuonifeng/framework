package com.codeages.framework.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ratelimiter")
public class RateLimiterProperties {
	private Map<String, String> paths = new HashMap<String, String>();
	
	public Map<String, String> getPaths() {
		return paths;
	}
	
	public Map<String, Long> getRateLimiterPatterns() {
		Map<String, Long> map = new HashMap<String, Long>(); 
		System.out.println(getPaths());
		for (Entry<String, String> entry : getPaths().entrySet()) {
			String value = entry.getValue();
			int slipIndex = value.indexOf(":");
			map.put(value.substring(0, slipIndex), Long.valueOf(value.substring(slipIndex+1)));
		}
		
		return map;
	}
}
