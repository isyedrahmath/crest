package com.capital.one.home.directs.controllers;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capital.one.home.directs.response.OpusResponse;
import com.capital.one.home.directs.services.OpusServices;

@Controller
@RequestMapping("/opus")
@PropertySource("classpath:config/application.properties")
public class OpusController {

	@Autowired
	OpusServices opusServices;
	
	
	@Autowired
	private Environment env;
	
	
	private static final Logger logger = Logger.getLogger(OpusController.class);
	
	@RequestMapping(value="/instant",method=RequestMethod.GET)
	@ResponseBody public OpusResponse getInstantTransactionDetailsFromDb( @RequestParam(value="sname", required=false) String sname){
		
		logger.info("Start of Transaction Request");
		OpusResponse response = new OpusResponse();
		if(sname == null)
			response.setObject(opusServices.getAllInstantTxnDetails());
		else
			response.setObject(opusServices.getServerSpecificInstantTxnDetails(sname));
		
		response.setStatusCode(200);
		response.setStatusMessage("Transaction Response send successfully");
		logger.info("Sending response for Transaction with status code : "+response.getStatusCode());
		return response;
	}
}
