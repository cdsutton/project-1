package com.revature.control;

import io.javalin.Javalin;

public interface ControllerACB {

	public abstract void mapEndpoints(Javalin app);
	
}