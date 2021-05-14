package com.revature.dao;

import javax.persistence.NoResultException;

import org.hibernate.Session;

import com.revature.connection.SessionUtility;
import com.revature.dto.PostLoginDTO;
import com.revature.exception.LoginException;
import com.revature.model.User;

public class UserRepository {
	
	public UserRepository() {
		super();
	}

	public User getUserByUsernameAndPassword(PostLoginDTO loginDTO) throws LoginException {
		 
		Session session = SessionUtility.getSessionFactory().openSession();
		
		User user;
		
		try {
			user = (User) session.createQuery("FROM User WHERE username=:un AND password=:pw")
				 .setParameter("un", loginDTO.getUsername())
				 .setParameter("pw", loginDTO.getPassword())
				 .getSingleResult();
		} catch (NoResultException e) {
				 	throw new LoginException("Invalid username and/or password");
			}
		return user;
	}

}
