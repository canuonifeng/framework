package com.codeages.framework.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.codeages.framework.authentication.PermissionsProvider;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PermissionConfig extends GlobalMethodSecurityConfiguration {

	private PermissionsProvider permissionProvider;
	
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(getPermissionEvaluator());
		return expressionHandler;
	}

	@Bean
	public PermissionEvaluator getPermissionEvaluator() {
		return new PermissionEvaluator() {
			@Override
			public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
				if (isAdmin()) {
					return true;
				}

				Set<String> permissions = permissionProvider.loadPermissions(authentication.getAuthorities());
				return permissions.contains(targetDomainObject.toString() + ":" + permission.toString());
			}

			@Override
			public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
					Object permission) {
				return false;
			}
		};
	}
	
	private boolean isAdmin() {
		return findCurrentUserRoleCodes().contains("ROLE_SUPER_ADMIN");
	}
	
	private Set<String> findCurrentUserRoleCodes() {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities();
		Set<String> roleCodes = new HashSet<String>();
		for (GrantedAuthority grantedAuthority : authorities) {
			roleCodes.add(grantedAuthority.getAuthority());
		}
		return roleCodes;
	}
}
