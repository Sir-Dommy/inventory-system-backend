package com.inventory.app.entity;

import lombok.AllArgsConstructor; 
import lombok.Data; 
import lombok.NoArgsConstructor; 

// This class handles the data needed for authentication which is the username and password
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest { 

	private String username; 
	private String password; 

}

