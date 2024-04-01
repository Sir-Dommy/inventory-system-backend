package com.inventory.app.controller;
import com.inventory.app.entity.AuthRequest; 
import com.inventory.app.entity.UserInfo; 
import com.inventory.app.service.JwtService; 
import com.inventory.app.service.UserInfoService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.web.bind.annotation.*; 

// This contoller handles user authentication and registration, ensuring passwords are encrypte by calling
// the JwtService
@RestController
@RequestMapping("/auth") 
public class UserController { 

	@Autowired
	private UserInfoService service; 

	@Autowired
	private JwtService jwtService; 

	@Autowired
	private AuthenticationManager authenticationManager; 

	// This is a test route, not required in the test
	@GetMapping("/welcome") 
	public String welcome() { 
		return "Welcome this endpoint is not secure"; 
	} 

	// this is a post method to create a new user, it require username and password
	@PostMapping("/addNewUser") 
	public ResponseEntity<Map<String, String>> addNewUser(@RequestBody UserInfo userInfo) { 
		Map<String, String> response = new HashMap<>();
		response.put("message",service.addUser(userInfo));
		return ResponseEntity.ok(response);
		// return service.addUser(userInfo); 
	}

	// This is the authentication method which utilizes the JwtService to create a beare token after a user is 
	// authicated using username and password, bearer token generated is used to authenticate requests made by users
	@PostMapping("/generateToken") 
	public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) { 
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())); 
		if (authentication.isAuthenticated()) { 
			Map<String, String> response = new HashMap<>();
        response.put("token", jwtService.generateToken(authRequest.getUsername()));
        return ResponseEntity.ok(response);
			// return jwtService.generateToken(authRequest.getUsername()); 
		} else { 
			throw new UsernameNotFoundException("invalid user request !"); 
		} 
	} 

} 

