package com.oauth2.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class Oauth2UserDetailsService implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null || username.isEmpty()) {
			throw new UsernameNotFoundException("Username is empty");
		}
		
		Authentication authentication = SecurityContextHolder.getContext()
		        .getAuthentication();

	    Object clientPrincipal = authentication.getPrincipal();
	    if (clientPrincipal instanceof User) {
	    		User user = ((User) clientPrincipal);
	    		return user;
	    }
	    throw new UsernameNotFoundException(
	        "Unauthorized client_id or username not found: " + username);
	}
}
