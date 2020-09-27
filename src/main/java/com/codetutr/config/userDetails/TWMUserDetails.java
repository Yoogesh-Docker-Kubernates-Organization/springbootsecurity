package com.codetutr.config.userDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codetutr.entity.User;

public class TWMUserDetails extends org.springframework.security.core.userdetails.User implements UserDetails{

	private static final long serialVersionUID = 59060339762385589L;
	
	private Map<String, Object> moreInfo = new HashMap<>();
	private User user;
	
	public TWMUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	public TWMUserDetails(UserDetails userDetails) {
		super(userDetails.getUsername(), userDetails.getPassword(), userDetails.isEnabled(), userDetails.isAccountNonExpired(), userDetails.isCredentialsNonExpired(), userDetails.isAccountNonLocked(), userDetails.getAuthorities());
	}

	public Map<String, Object> getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(Map<String, Object> moreInfo) {
		this.moreInfo = moreInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

