package com.codeages.framework.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		List a;
		if (!"POST".equals(request.getMethod())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		String username = "", password = "";

		if (null != this.obtainUsername(request) && null != this.obtainPassword(request)) {
			username = this.obtainUsername(request);
			password = this.obtainPassword(request);
		} else {
			Map<String, String> map = readBody(request);
			username = map.get("username");
			password = map.get("password");
		}

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	private Map<String, String> readBody(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			InputStream in = request.getInputStream();
			BufferedInputStream inputStream = new BufferedInputStream(in);

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			while (null != (line = bufferedReader.readLine())) {
				stringBuffer.append(line);
			}

			bufferedReader.close();
			inputStream.close();

			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(stringBuffer.toString(), Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<String, String>();
	}
}
