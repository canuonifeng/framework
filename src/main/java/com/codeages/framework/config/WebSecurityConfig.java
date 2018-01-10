package com.codeages.framework.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import com.codeages.framework.web.AuthenticationFilter;
import com.codeages.framework.web.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailService;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.rememberMe().rememberMeServices(rememberMeServices());
		http.addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.formLogin().successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);
		http.logout().logoutSuccessHandler(logoutSuccessHandler);
		http.authorizeRequests().antMatchers("/csrf-token").permitAll().anyRequest().authenticated();
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(getAccessDeniedHandler());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(encoder);
		ReflectionSaltSource saltSource = new ReflectionSaltSource();
		saltSource.setUserPropertyToUse("salt");
		authenticationProvider.setSaltSource(saltSource);
		auth.authenticationProvider(authenticationProvider);
	}
	
	@Bean
	public Filter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authFilter = new AuthenticationFilter();
		authFilter.setAuthenticationManager(authenticationManagerBean());
		authFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		authFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		return authFilter;
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
		rememberMeServices.setAlwaysRemember(true);
		return rememberMeServices;
	}

	@Bean
	public HttpSessionCsrfTokenRepository getCsrfTokenRepository() {
		return new HttpSessionCsrfTokenRepository();
	}

	@Bean
	public AccessDeniedHandler getAccessDeniedHandler() {
		return new AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				ResponseWrapper err = new ResponseWrapper(accessDeniedException.getMessage());
				err.setMessage(accessDeniedException.getMessage());
				err.setStatus(String.valueOf(HttpServletResponse.SC_FORBIDDEN));

				ObjectMapper mapper = new ObjectMapper();
				response.getWriter().append(mapper.writeValueAsString(err));
				response.setContentType("application/json");
				response.setStatus(200);
			}
		};
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {
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
		};
	}

}