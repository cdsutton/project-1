package com.revature.connection;

import java.sql.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.model.Reimbursement;
import com.revature.model.Role;
import com.revature.model.Status;
import com.revature.model.Type;
import com.revature.model.User;

public class Hibernate {
	public static void populateData() {
		Session session = SessionUtility.getSessionFactory().openSession();
		
		//START IN CREATE MODE: creates new database tables based on existing class mappings.
		//If the tables already exist, it will drop the existing tables and recreate them
		//<property name="hbm2ddl.auto">create</property>
		
		Transaction txStatus = session.beginTransaction();
		
		Status pending = new Status("pending");
		Status approved = new Status("approved");
		Status denied = new Status("denied");
		
		session.save(pending);
		session.save(approved);
		session.save(denied);
		
		txStatus.commit();
		
		Transaction txType = session.beginTransaction();
		
		Type lodging = new Type("LODGING");
		Type travel = new Type("TRAVEL");
		Type food = new Type("FOOD");
		Type other = new Type("OTHER");
		
		session.save(lodging);
		session.save(travel);
		session.save(food);
		session.save(other);
		
		txType.commit();
		
		Transaction txRole = session.beginTransaction();
		
		Role realtor = new Role("realtor");
		Role manager = new Role("manager");
		
		session.save(realtor);
		session.save(manager);
		
		txRole.commit();
		
		Transaction txUser = session.beginTransaction();
		
		Role realtorId = session.get(Role.class, 1);
		Role managerId = session.get(Role.class, 2);
		
		//Create new users
		User c001 = new User("brainChild001", "1pacify1!", "Ivan", "Whisky", "iw001@cyborg.nine", realtorId);
		User c002 = new User("fighterJet002", "2skysthelimit2@", "Jet", "Link", "jl002@cyborg.nine", realtorId);
		User c003 = new User("balletDancer003", "3jean3#", "Francoise", "Arnoul", "fa003@cyborg.nine", realtorId);
		User c004 = new User("livingWeapon004", "4hilda4$", "Albert", "Heinrich", "ah004@cyborg.nine", realtorId);
		User c005 = new User("stoicEarth005", "5naturecalls5%", "Geronimo", "Junior", "gj005@cyborg.nine", realtorId);
		User c006 = new User("masterChef006", "6myrestaurant6^", "Chang", "Changku", "cc006@cyborg.nine", realtorId);
		User c007 = new User("smoothActor007", "7sophie7&", "Sir", "Greatbritain", "sg007@cyborg.nine", realtorId);
		User c008 = new User("oneBreath008", "8kaboremasmado8*", "Pyunma", "Eight", "pe008@cyborg.nine", realtorId);
		User c009 = new User("highSpeed009", "9yasu9(", "Joe", "Shimamura", "js009@cyborg.nine", realtorId);
		User c010 = new User("professorDoctor000", "0cyborgfamily0)", "Isaac", "Gilmore", "ig000@cyborg.nine", managerId);
		User c011 = new User("plusUltra010", "1twin0+", "Cory", "Button", "cb010@cyborg.nine", realtorId);
		User c012 = new User("minusUltra010", "1brothers0-", "Coby", "Glutton", "cg010@cyborg.nine", realtorId); 
		
		//Users are currently transient
		
		session.save(c001);
		session.save(c002);
		session.save(c003);
		session.save(c004);
		session.save(c005);
		session.save(c006);
		session.save(c007);
		session.save(c008);
		session.save(c009);
		session.save(c010);
		session.save(c011);
		session.save(c012);
		
		//Now users are persistent
		
		txUser.commit();
		
		Transaction txReimbursement = session.beginTransaction();
		
		Status pendingId = session.get(Status.class, 1);
		Status approvedId = session.get(Status.class, 2);
		Status deniedId = session.get(Status.class, 3);
		
		Type lodgingId = session.get(Type.class, 1);
		Type travelId = session.get(Type.class, 2);
		Type foodId = session.get(Type.class, 3);
		Type otherId = session.get(Type.class, 4);
		
		User c001Id = session.get(User.class, 1);
		User c002Id = session.get(User.class, 2);
		User c003Id = session.get(User.class, 3);
		User c010Id = session.get(User.class, 11);
		User c000Id = session.get(User.class, 10);
		
		Date currentTime = new Date(System.currentTimeMillis());
		
		Reimbursement r001 = new Reimbursement(100, currentTime, currentTime, "I require a significant amount of baby food.", null, c001Id, c000Id, approvedId, foodId);
		Reimbursement r002 = new Reimbursement(200, currentTime, currentTime, "Goin' to New York", null, c002Id, c000Id, deniedId, travelId);
		Reimbursement r003 = new Reimbursement(300, currentTime, null, "I wanted to get something special for Joe...", null, c003Id, null, pendingId, otherId);
		Reimbursement r004 = new Reimbursement(10000, currentTime, null, "For the hotel me and I my brother need to stay at!", null, c010Id, null, pendingId, lodgingId);
		
		session.save(r001);
		session.save(r002);
		session.save(r003);
		session.save(r004);
		
		txReimbursement.commit();
		session.close();
	}
	
}
