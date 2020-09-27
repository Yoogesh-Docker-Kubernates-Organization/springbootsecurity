package com.codetutr.config.userDetails;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.codetutr.entity.Authority;
import com.codetutr.entity.User;

public class TWMPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if (!(targetDomainObject instanceof User)) {
			return false;
		} else {
			User dbaUser = (User) targetDomainObject;
			
			if(!dbaUser.getAuthorities().isEmpty()){
				List<Authority> authorities = dbaUser.getAuthorities();
				for (Authority next : authorities) {
					if(next.getRole().equals("ROLE_DBA")){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * We need to override this method as well. To keep things simple, Let's not use
	 * this, throwing exception is someone uses it.
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {

		throw new UnsupportedOperationException("hasPermission() by ID is not supported");
	}

}
