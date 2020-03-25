package com.codeages.framework.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.codeages.framework.authentication.AuthenticationFilter;
import com.codeages.framework.handler.AuthenticationHandler;

public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.logout().logoutSuccessHandler(getLogoutSuccessHandler());
		http.antMatcher("/**").authorizeRequests().anyRequest().authenticated();
		http.formLogin().successHandler(getAuthenticationHandler()).failureHandler(getAuthenticationHandler());
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(getAccessDeniedHandler());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**", "/", "/login**", "/logout", "/csrf-token", "/index.html");
	}

	@Bean
	public Filter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authFilter = new AuthenticationFilter();
		authFilter.setAuthenticationManager(authenticationManagerBean());
		authFilter.setAuthenticationFailureHandler(getAuthenticationHandler());
		authFilter.setAuthenticationSuccessHandler(getAuthenticationHandler());
		return authFilter;
	}

	@Bean
	public AuthenticationHandler getAuthenticationHandler() {
		return new AuthenticationHandler();
	}

	@Bean
	public LogoutSuccessHandler getLogoutSuccessHandler() {
		return new com.codeages.framework.handler.LogoutSuccessHandler();
	}

	@Bean
	public AccessDeniedHandler getAccessDeniedHandler() {
		return new com.codeages.framework.handler.AccessDeniedHandler();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new com.codeages.framework.authentication.AuthenticationEntryPoint();
	}
}