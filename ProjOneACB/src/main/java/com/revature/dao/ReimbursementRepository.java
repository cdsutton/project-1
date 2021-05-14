package com.revature.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.SQLException;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.connection.SessionUtility;
import com.revature.dto.PostReimbursementDTO;
import com.revature.dto.ReimbReturn;
import com.revature.exception.BadParameterException;
import com.revature.exception.DatabaseException;
import com.revature.exception.LoginException;
import com.revature.model.Reimbursement;
import com.revature.model.Status;
import com.revature.model.Type;
import com.revature.model.User;
import com.revature.connection.SessionUtility;

public class ReimbursementRepository {

	public ReimbursementRepository() {
		super();
	}

	public ReimbReturn addReimbursement(PostReimbursementDTO reimbursementDTO, User user) throws DatabaseException, SQLException {

		Transaction txAdd = null;

		Date currentTime = new Date(System.currentTimeMillis());

		Reimbursement reimbursement = new Reimbursement();
		reimbursement.setAmount(reimbursementDTO.getAmount());
		reimbursement.setDescription(reimbursementDTO.getDescription());
		reimbursement.setSubmitted(currentTime);
		reimbursement.setAuthor(user);

		String TypeDTO = reimbursementDTO.getType();

		try (Session session = SessionUtility.getSessionFactory().openSession()) {
			txAdd = session.beginTransaction();

			// We need to query for the actual type object based on our string "lodging",
			// "food", etc.
			Type type = (Type) session.createQuery("FROM Type t WHERE t.typeName=:type").setParameter("type", TypeDTO)
					.getSingleResult();

			Status status = (Status) session.createQuery("FROM Status s WHERE s.statusName='pending'")
					.getSingleResult();

			reimbursement.setStatus(status);
			reimbursement.setType(type); // set type

			session.persist(reimbursement); // Persist the reimbursement object to the database

			txAdd.commit();

			String author = user.getFirstName() + " " + user.getLastName();

			// Check if there is an image stored
			byte[] byteArr = null;
			if (reimbursement.getReceipt() != null) {
				byteArr = reimbursement.getReceipt().getBytes(1, (int) reimbursement.getReceipt().length());
			}

			ReimbReturn reimbReturn = new ReimbReturn(reimbursement.getReimbId(), reimbursement.getAmount(),
					reimbursement.getSubmitted(), null, reimbursement.getDescription(), byteArr, author, null,
					reimbursement.getStatus().getStatusName(), reimbursement.getType().getTypeName());
			session.close();
			return reimbReturn;
		} catch (NoResultException e) {
			throw new DatabaseException("Cannot add reimbursement at this time!");
		} catch (SQLException e) {
			throw new DatabaseException(
					"Something went wrong with the database! " + "Exception message: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<ReimbReturn> viewReimbursements(User user, String role) throws DatabaseException {

		try (Session session = SessionUtility.getSessionFactory().openSession()) {

			List<ReimbReturn> reimbReturnList = new ArrayList<>();
			List<Reimbursement> reimbursements;
			if (role.equals("realtor")) {
				reimbursements = (List<Reimbursement>) session.createQuery("FROM Reimbursement WHERE author=:au")
						.setParameter("au", user).getResultList();
			} else {
				reimbursements = (List<Reimbursement>) session.createQuery("FROM Reimbursement").getResultList();
			}
			for (Reimbursement reimbursement : reimbursements) {
				String author = reimbursement.getAuthor().getFirstName() + " "
						+ reimbursement.getAuthor().getLastName();
				String resolver = null;
				byte[] byteArr = null;
				if (reimbursement.getResolver() != null) {
					resolver = reimbursement.getResolver().getFirstName() + " "
							+ reimbursement.getResolver().getLastName();
				}
				if (reimbursement.getReceipt() != null) {
					byteArr = reimbursement.getReceipt().getBytes(1, (int) reimbursement.getReceipt().length());
				}
				ReimbReturn reimbReturn = new ReimbReturn(reimbursement.getReimbId(), reimbursement.getAmount(),
						reimbursement.getSubmitted(), reimbursement.getResolved(), reimbursement.getDescription(),
						byteArr, author, resolver, reimbursement.getStatus().getStatusName(),
						reimbursement.getType().getTypeName());
				reimbReturnList.add(reimbReturn);
			}
			session.close();
			return reimbReturnList;
		} catch (NoResultException e) {
			throw new DatabaseException("No results were returned!");
		} catch (SQLException e) {
			throw new DatabaseException(
					"Something went wrong with the database! " + "Exception message: " + e.getMessage());
		}
	}

	public void approveReimbursement(User user, String reimbId) throws BadParameterException, DatabaseException, LoginException {
	
		try (Session session = SessionUtility.getSessionFactory().openSession()) {
			
			if (user.getRole().getRoleName().equals("realtor")) {
				throw new LoginException("Only managers can approve reimbursements!");
			}
			
			Transaction txApprove = session.beginTransaction();
			
			Date currentTime = new Date(System.currentTimeMillis());
			Status approved = session.get(Status.class, 2);
			
			int intId = Integer.parseInt(reimbId);
			Reimbursement approvedReimbursement = session.get(Reimbursement.class, intId);
			
			if (approvedReimbursement == null) {
				throw new BadParameterException("This reimbursement does not exist!");
			}
			
			if (!approvedReimbursement.getStatus().getStatusName().equals("pending")) {
				throw new BadParameterException("You only can approve pending reimbursements!");
			}
			
			approvedReimbursement.setResolver(user);
			approvedReimbursement.setStatus(approved);
			approvedReimbursement.setResolved(currentTime);
			
			session.persist(approvedReimbursement);
			txApprove.commit();
			
		} catch (NoResultException e) {
			throw new DatabaseException("Could not perform operation!");
		}
	}
	
	public void denyReimbursement(User user, String reimbId) throws BadParameterException, DatabaseException, LoginException {

		try (Session session = SessionUtility.getSessionFactory().openSession()) {
			
			if (user.getRole().getRoleName().equals("realtor")) {
				throw new LoginException("Only managers can deny reimbursements!");
			}
			
			Transaction txDeny = session.beginTransaction();
			
			Date currentTime = new Date(System.currentTimeMillis());
			Status denied = session.get(Status.class, 3);
			
			int intId = Integer.parseInt(reimbId);
			Reimbursement deniedReimbursement = session.get(Reimbursement.class, intId);
			
			if (deniedReimbursement == null) {
				throw new BadParameterException("This reimbursement does not exist!");
			}
			
			if (!deniedReimbursement.getStatus().getStatusName().equals("pending")) {
				throw new BadParameterException("You only can deny pending reimbursements!");
			}
			
			deniedReimbursement.setResolver(user);
			deniedReimbursement.setStatus(denied);
			deniedReimbursement.setResolved(currentTime);
			
			session.persist(deniedReimbursement);
			txDeny.commit();
			
		} catch (NoResultException e) {
			throw new DatabaseException("Could not perform operation!");
		}
	}
}