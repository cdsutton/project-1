package com.revature.control;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import com.revature.dto.MessageDTO;
import com.revature.dto.PostReimbursementDTO;
import com.revature.dto.ReimbReturn;
import com.revature.exception.BadParameterException;
import com.revature.model.User;
import com.revature.service.ReimbursementService;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;

public class ReimbursementController implements ControllerACB {
	
	private ReimbursementService reimbursementService;
		
		public ReimbursementController() {
			this.reimbursementService = new ReimbursementService();
		}
		
		private Handler addReimbursement = ctx -> {
			
			User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
			ctx.contentType("multipart/form-data");
			
			if (ctx.formParam("amount").equals("") || ctx.formParam("description").equals("") || ctx.formParam("type").equals("")) {
				throw new BadParameterException("All fields except receipt are required");
			}
			
			PostReimbursementDTO reimbursementDTO = new PostReimbursementDTO();
			reimbursementDTO.setAmount(Double.parseDouble(ctx.formParam("amount")));
			reimbursementDTO.setDescription(ctx.formParam("description"));
			reimbursementDTO.setType(ctx.formParam("type"));
			
//			if (ctx.uploadedFiles().size() != 0) {
//				UploadedFile uploadedFile = ctx.uploadedFiles().get(0);
//				InputStream byteStream = uploadedFile.component1();
//				byte[] byteArray = byteStream.readAllBytes();
//				Blob blob = new SerialBlob(byteArray);
//				reimbursementDTO.setReceipt(blob);
//			} else reimbursementDTO.setReceipt(null);
			
			ReimbReturn reimbursement = this.reimbursementService.addReimbursement(reimbursementDTO, user);
			ctx.json(reimbursement);
			ctx.status(201);
		};
		
		private Handler approveReimbursement = (ctx) -> {
			User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
			String reimbId = ctx.pathParam("reimbId");
			
			reimbursementService.approveReimbursement(user, reimbId);
			
			ctx.json(new MessageDTO("Reimbursement approved!"));
			ctx.status(200);
		};
		
		private Handler denyReimbursement = (ctx) -> {
			User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
			String reimbId = ctx.pathParam("reimbId");
			
			reimbursementService.denyReimbursement(user, reimbId);
			
			ctx.json(new MessageDTO("Reimbursement denied!"));
			ctx.status(200);
		};
		
		private Handler viewReimbursements = ctx -> {
			User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
			List<ReimbReturn> reimbursements = this.reimbursementService.viewReimbursements(user);
			ctx.json(reimbursements);
			ctx.status(200);
		};
		
		@Override
		public void mapEndpoints(Javalin app) {
			
			app.post("/reimbursements", addReimbursement);
			app.post("/reimbursements/:reimbId/approve", approveReimbursement);
			app.post("/reimbursements/:reimbId/deny", denyReimbursement);
			app.get("/reimbursements", viewReimbursements);
	
		}
	
}
