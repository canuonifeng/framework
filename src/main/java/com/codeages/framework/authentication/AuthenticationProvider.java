package com.codeages.framework.authentication;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

public class AuthenticationProvider extends DaoAuthenticationProvider{
	
	public AuthenticationProvider(UserDetailsService userDetailsService) {
		super();
		this.setUserDetailsService(userDetailsService);
		this.setPasswordEncoder(new Md5PasswordEncoder());
		ReflectionSaltSource saltSource = new ReflectionSaltSource();
		saltSource.setUserPropertyToUse("salt");
		this.setSaltSource(saltSource);
	}
}
