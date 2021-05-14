package com.revature.service;

import java.sql.SQLException;
import java.util.List;

import com.revature.dao.ReimbursementRepository;
import com.revature.dto.PostReimbursementDTO;
import com.revature.dto.ReimbReturn;
import com.revature.exception.BadParameterException;
import com.revature.exception.DatabaseException;
import com.revature.exception.LoginException;
import com.revature.model.User;

public class ReimbursementService {
	
private ReimbursementRepository reimbursementRepository;
	
	public ReimbursementService() {
		this.reimbursementRepository = new ReimbursementRepository();
	}
	
	// This one is for Mockito
	public ReimbursementService(ReimbursementRepository reimbursementRepository) {
		this.reimbursementRepository = reimbursementRepository;
	}
	
	public ReimbReturn addReimbursement(PostReimbursementDTO reimbursementDTO, User user) throws BadParameterException, DatabaseException, SQLException {
		
		if (user == null) {
			throw new BadParameterException("You must be logged in to create a reimbursement!");
		}
		
		return reimbursementRepository.addReimbursement(reimbursementDTO, user);
	}
	
	public void approveReimbursement(User user, String reimbId) throws BadParameterException, DatabaseException, LoginException {

		if (user == null) {
			throw new BadParameterException("You must be logged in to approve a reimbursement!");
		}
		
		reimbursementRepository.approveReimbursement(user, reimbId);
		
	}
	
	public void denyReimbursement(User user, String reimbId) throws BadParameterException, DatabaseException, LoginException {

		if (user == null) {
			throw new BadParameterException("You must be logged in to deny a reimbursement!");
		}
		
		reimbursementRepository.denyReimbursement(user, reimbId);
		
	}
	
	public List<ReimbReturn> viewReimbursements(User user) throws BadParameterException, DatabaseException, LoginException {
		
		if (user == null) {
			throw new LoginException("Unauthorized access! No user currently logged in!");
		}
		
		return reimbursementRepository.viewReimbursements(user, user.getRole().getRoleName());
	}
	
}
