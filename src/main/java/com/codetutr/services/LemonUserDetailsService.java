package com.codetutr.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codetutr.entity.Authority;
import com.codetutr.entity.User;


@Service
public class LemonUserDetailsService implements UserDetailsService 
{
	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		if(username == null)
		{
			return null;
		}
		
		User dbUser = userService.getUserByUserName(username);
		
		if(dbUser == null)
		{
			return null;  // Spring security will handle it
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Authority next : dbUser.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority(next.getRole()));
		}
		
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(dbUser.getUsername(), dbUser.getPassword(), true, true, true, true, authorities);
		
		return userDetails;
	}

}
