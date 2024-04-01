package com.inventory.app.service;

import com.inventory.app.entity.UserInfo; 
import com.inventory.app.repository.UserInfoRepository; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service; 

import java.util.Optional; 

// This is a service class which helps to find a user by user name and also creating a new user
@Service
public class UserInfoService implements UserDetailsService { 

	@Autowired
	private UserInfoRepository repository; 

	@Autowired
	private PasswordEncoder encoder; 

	// finding user by username
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 

		Optional<UserInfo> userDetail = repository.findByName(username); 

		// Converting userDetail to UserDetails 
		return userDetail.map(UserInfoDetails::new) 
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username)); 
	} 

	// creating a new user
	public String addUser(UserInfo userInfo) { 
		userInfo.setPassword(encoder.encode(userInfo.getPassword())); 
		repository.save(userInfo); 
		return "User: "+userInfo.getName()+" Added Successfully "; 
	} 


} 
