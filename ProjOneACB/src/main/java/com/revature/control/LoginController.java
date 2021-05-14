package com.revature.control;

import com.revature.dto.PostLoginDTO;
import com.revature.dto.MessageDTO;
import com.revature.model.User;
import com.revature.service.LoginService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class LoginController implements ControllerACB {

	private LoginService loginService;
	
	public LoginController() {
		this.loginService = new LoginService();
	}
	
	private Handler loginHandler = (ctx) -> {
		PostLoginDTO loginDTO = ctx.bodyAsClass(PostLoginDTO.class);
		
		User user = loginService.login(loginDTO);
		
		ctx.sessionAttribute("currentlyLoggedInUser", user);
		ctx.json(user);
		ctx.status(200);
	};
	
	private Handler currentUserHandler = (ctx) -> {
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		
		if (user == null) {
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User is not currently logged in!");
			ctx.json(messageDTO);
			ctx.status(400);
		} else {
			ctx.json(user);
			ctx.status(200);
		}
		
	};
	
	private Handler logoutHandler = (ctx) -> {
		
		ctx.req.getSession().invalidate();
		ctx.status(200);
		
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		
		app.post("/login", loginHandler);
		app.get("/current_user", currentUserHandler);
		app.post("/logout", logoutHandler);

	}

}