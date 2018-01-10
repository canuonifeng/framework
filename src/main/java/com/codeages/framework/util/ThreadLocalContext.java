package com.codeages.framework.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class ThreadLocalContext {
	public static UserDetails getCurrentUser() {
		return (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
	}
}
