//package com.revature.main;
//
//import java.util.List;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import com.revature.connection.SessionUtility;
//import com.revature.model.Role;
//import com.revature.model.User;
//
//
//public class HibernateTesting {

	
		
		//Now run the java application!
		
		//CHANGE TO VALIDATE MODE: validate existing tables against class mappings. If validation fails,
		//application will not work properly
		//<property name="hbm2ddl.auto">validate</property>
		
//		User user1 = session.get(User.class, 1); // When we use .get, our ship object is in the persistent state
//		System.out.println(user1);
//		
//		Transaction tx = session.beginTransaction();
//		
//		User user2 = new User(0, "fighterJet002", "2skythelimit2@", "Jet", "Link", "jl002@cyborg.nine");
//		
//		session.save(user2);
//		
//		//user.setShip(ship); // I can actually set my pirate to this particular ship
//		
//		tx.commit();
//		
//		//Pirate p2 = session.get(Pirate.class, 1);
//		System.out.println(user2);
		
		// HQL EXAMPLE
		// Pirate edward = (Pirate) session.createQuery("FROM Pirate p WHERE p.firstName = 'Edward'").getSingleResult();
		
		// GETTING ALL PIRATES (Example of HQL)
		
		// List<Pirate> pirates = session.createQuery("FROM Pirate p").getResultList();
		// System.out.println(pirates);
		
		// GETTING PIRATES THAT BELONG TO SHIP W/ ID OF 1 (Example of HQL)
		
		// List<Pirate> piratesOfShipId1 = session.createQuery("SELECT p FROM Pirate p JOIN p.ship s WHERE s.id = 1").getResultList();
		// System.out.println("pirates of ship id 1: " + piratesOfShipId1);
		
		// System.out.println(edward);
		// System.out.println(edward.getShip());
		
		// With Hibernate, we still follow the 3 layered architecture of the Controller layer, service layer,
		// and dao layer. 
		
		// Hibernate operations will occur over on the dao layer.
		
		// So, for project-1, use Hibernate, because it will make things a lot easier
		// Just get past the initial setup of Hibernate and the mapping of Entities
		
		/*
		 * 
		 */
		
//		Ship ship1 = session.find(Ship.class, 1);
//		// What Hibernate object state is ship1 in?
//		// Persistent, because when you retrieve a new object from the database through Hibernate,
//		// you will be given a persistent object.
//		// MAKE SURE TO SWITCH TO UPDATE MODE TO ADD SHIPDETAIL MAPPING! THEN SWITCH BACK TO VALIDATE MODE!
//		////<property name="hbm2ddl.auto">update</property>
//		
//		Transaction tx1 = session.beginTransaction();
//		
//		ShipDetail sd = new ShipDetail(100, "a ship"); // This is a transient object, because it is not associated
//		// with Hibernate
//		
//		// session.persist(sd); 
//		// By saving the object, we make it persistent (meaning an entry in the DB will also be created)
//			// and then we will not have the issue with 'TransientObjectException: 
//				// object references an unsaved transient instance - save the transient instance -
//				// before flushing: com.revature.models.ShipDetail'
//		
//		// Instead of having to persist the ShipDetail, then setting the shipDetail of our ship to that object,
//		// we can instead do both operations from ship1, by having a cascade type set for our relation
//		
//		// ship1.setShipName("Black Pearl"); // we can change the ship name, and once we commit,
//		// the value will be changed on the database
//		
//		ship1.setShipDetail(sd);
//		session.merge(ship1);
//		
//		tx1.commit(); // When you have a persistent object, such as ship1,
//		// when we commit a transaction or use session.flush(),
//		// it synchronize what was done here with the database and vice versa
		
//	}
//}