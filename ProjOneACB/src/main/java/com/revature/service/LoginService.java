package com.revature.service;

import com.revature.dao.UserRepository;
import com.revature.dto.PostLoginDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.LoginException;
import com.revature.model.User;

public class LoginService {

	private UserRepository userRepository;
	
	public LoginService() {
		this.userRepository = new UserRepository();
	}
	
	// This one is for Mockito
	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User login(PostLoginDTO loginDTO) throws BadParameterException, LoginException {
		// Do some checking for blank username, blank password
		if (loginDTO.getUsername().trim().equals("") || loginDTO.getPassword().trim().equals("")) {
			throw new BadParameterException("Cannot have blank username and/or password");
		}
		
		User user = userRepository.getUserByUsernameAndPassword(loginDTO);
		
		if (user == null) {
			throw new LoginException("User was not able to login in with the supplied username and password");
		}
		
		return user;
	}
	
}