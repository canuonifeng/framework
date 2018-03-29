package com.codeages.framework.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.codeages.framework.response.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogoutSuccessHandler
		implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		response.getWriter().append(mapper.writeValueAsString(new ResponseWrapper<Map<String,String>>(map)));
		response.setContentType("application/json");
		response.setStatus(200);

	}
}
