package com.codeages.framework.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.util.concurrent.RateLimiter;

@Component
public class RateLimiterInterceptor extends HandlerInterceptorAdapter {

	private Map<String, RateLimiter> map = new HashMap<String, RateLimiter>();

	@Autowired
	private RateLimiterProperties rateLimiterProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String path = request.getPathInfo();
		Map<String, Long> paths = rateLimiterProperties.getRateLimiterPatterns();
		
		for (Entry<String, Long> entry : paths.entrySet()) {
			String key = entry.getKey();
			if (key != null && path != null && path.indexOf(key) == 0) {
				if (!map.containsKey(key)) {
					map.put(key, RateLimiter.create(entry.getValue()));
				}
				
				RateLimiter rateLimiter = map.get(key);
				if (rateLimiter.tryAcquire()) {
					rateLimiter.acquire();
				} else {
					return false;
				}
			}
		}

		return true;
	}
}
