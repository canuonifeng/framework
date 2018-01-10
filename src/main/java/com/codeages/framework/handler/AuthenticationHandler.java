package com.codeages.framework.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.codeages.framework.response.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
	//
	// @Autowired
	// private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// User currenUser = (User)
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// User user = userService.getUserById(currenUser.getId());
		// user.setLastLoginTime(new Date());
		// user.setLastLoginIp(getIpAddr(request));
		// userService.updateUser(user);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		map.put("token", request.getSession().getId());
		response.getWriter().append(mapper.writeValueAsString(new ResponseWrapper(map)));
		response.setContentType("application/json");
		response.setStatus(200);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		Map<String, String> map = new HashMap<String, String>();
		ResponseWrapper responseWrapper = new ResponseWrapper(map);
		responseWrapper.setStatus(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED) + ".1");
		responseWrapper.setMessage("用户名或密码错误");

		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().append(mapper.writeValueAsString(responseWrapper));
		response.setContentType("application/json");
		response.setStatus(200);
	}

	private String getIpAddr(HttpServletRequest request) {
		String proxs[] = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR" };
		String ip = null;
		for (String prox : proxs) {
			ip = request.getHeader(prox);
			if (null == ip || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
				continue;
			} else {
				break;
			}
		}
		if (null == ip || ip.isEmpty()) {
			return request.getRemoteAddr();
		}
		return ip;
	}

}
