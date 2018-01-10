package com.codeages.framework.authentication;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

public interface PermissionsProvider {

	Set<String> loadPermissions(Collection<? extends GrantedAuthority> authentication);
	
}
