package com.graymatter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.graymatter.configuration.UserPrincipal;
import com.graymatter.entities.User;
import com.graymatter.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= repository.findByEmail(username).get();
		if(user==null)
			throw new UsernameNotFoundException(username);
		return new UserPrincipal(user);
	}

}
