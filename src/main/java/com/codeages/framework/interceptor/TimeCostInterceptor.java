package com.codeages.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class TimeCostInterceptor extends HandlerInterceptorAdapter {

	public final static Logger logger = LoggerFactory.getLogger(TimeCostInterceptor.class);
	private ThreadLocal<Long> timeThreadLocal = new ThreadLocal<Long>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long beginTime = System.currentTimeMillis();
		timeThreadLocal.set(beginTime);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long endTime = System.currentTimeMillis();
		long beginTime = timeThreadLocal.get();
		if (endTime - beginTime > 500) {
			logger.warn(request.getRequestURI() + " 比较耗时：" + (endTime - beginTime));
		}
	}
}
