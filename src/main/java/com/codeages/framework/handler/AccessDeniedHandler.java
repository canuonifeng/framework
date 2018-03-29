package com.codeages.framework.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

import com.codeages.framework.response.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ResponseWrapper<String> err = new ResponseWrapper<String>(accessDeniedException.getMessage());
		err.setMessage(accessDeniedException.getMessage());
		err.setStatus(String.valueOf(HttpServletResponse.SC_FORBIDDEN));

		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().append(mapper.writeValueAsString(err));
		response.setContentType("application/json");
		response.setStatus(200);
	}
}
