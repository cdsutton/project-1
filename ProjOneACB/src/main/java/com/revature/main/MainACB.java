package com.revature.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.connection.Hibernate;
import com.revature.control.ControllerACB;
import com.revature.control.ExceptionController;
import com.revature.control.LoginController;
import com.revature.control.ReimbursementController;

import io.javalin.Javalin;

public class MainACB {

	private static Logger logger = LoggerFactory.getLogger(MainACB.class);
	
	public static void main(String[] args) {
		Javalin app = Javalin.create((config) -> {
			config.addStaticFiles("static");
//			config.enableCorsForAllOrigins();
//			config.enableCorsForOrigin("http://somewebsite.com");
		});
		
		Hibernate.populateData();
		
		mapControllers(app, new LoginController(), new ReimbursementController(), new ExceptionController());
		
		app.start(7009);
	}
	
	private static void mapControllers(Javalin app, ControllerACB... controllers) {
		for (ControllerACB c : controllers) {
			c.mapEndpoints(app);
		}
	}

}