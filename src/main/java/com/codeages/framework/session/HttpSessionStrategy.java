package com.codeages.framework.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.Session;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;

@Configuration
@EnableJdbcHttpSession()
public class HttpSessionStrategy implements MultiHttpSessionStrategy {
	CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
	HeaderHttpSessionStrategy headerHttpSessionStrategy = new HeaderHttpSessionStrategy();

	@Override
	public String getRequestedSessionId(HttpServletRequest request) {
		String sessionId = headerHttpSessionStrategy.getRequestedSessionId(request);
		if (null == sessionId) {
			sessionId = cookieHttpSessionStrategy.getRequestedSessionId(request);
		}
		return sessionId;
	}

	@Override
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		headerHttpSessionStrategy.onNewSession(session, request, response);
		cookieHttpSessionStrategy.onNewSession(session, request, response);
	}

	@Override
	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		headerHttpSessionStrategy.onInvalidateSession(request, response);
		cookieHttpSessionStrategy.onInvalidateSession(request, response);
	}

	@Override
	public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response) {
		return cookieHttpSessionStrategy.wrapRequest(request, response);
	}

	@Override
	public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response) {
		return cookieHttpSessionStrategy.wrapResponse(request, response);
	}

}
