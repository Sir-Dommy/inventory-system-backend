package com.inventory.app.service;

import com.inventory.app.entity.UserInfo; 
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetails; 

import java.util.Collection; 
import java.util.List; 

// This class is utilised by JwtService to help in user authentication and enable bearer token
// generation
public class UserInfoDetails implements UserDetails { 

	private String name; 
	private String password; 
	private List<GrantedAuthority> authorities; 

	public UserInfoDetails(UserInfo userInfo) { 
		name = userInfo.getName(); 
		password = userInfo.getPassword();  
	} 

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { 
		return authorities; 
	} 

	@Override
	public String getPassword() { 
		return password; 
	} 

	@Override
	public String getUsername() { 
		return name; 
	} 

	@Override
	public boolean isAccountNonExpired() { 
		return true; 
	} 

	@Override
	public boolean isAccountNonLocked() { 
		return true; 
	} 

	@Override
	public boolean isCredentialsNonExpired() { 
		return true; 
	} 

	@Override
	public boolean isEnabled() { 
		return true; 
	} 
} 

