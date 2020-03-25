package com.codeages.framework.authentication;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

public class AuthenticationProvider extends DaoAuthenticationProvider{
	
	public AuthenticationProvider(UserDetailsService userDetailsService) {
		super();
		this.setUserDetailsService(userDetailsService);
	}
}
