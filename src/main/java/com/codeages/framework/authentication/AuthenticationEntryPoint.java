package com.codeages.framework.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;

import com.codeages.framework.response.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint{
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		Map<String, String> map = new HashMap<String, String>();
		ResponseWrapper responseWrapper = new ResponseWrapper(map);
		responseWrapper.setStatus(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
		responseWrapper.setMessage(authException.getMessage());

		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().append(mapper.writeValueAsString(responseWrapper));
		response.setContentType("application/json");
		response.setStatus(200);
	}
}
