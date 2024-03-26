package com.app.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.exception.InvalidInputException;
import com.app.model.UserDO;
import com.app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	 @Autowired
	 private UserRepository userRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    
	        UserDO user = userRepository.findByMobileNo(username);
	        if(user == null){
	        
	            throw new InvalidInputException("could not found user..!!");
	        }
	   
			return user;
	}

}
