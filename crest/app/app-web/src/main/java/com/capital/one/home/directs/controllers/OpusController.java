package com.capital.one.home.directs.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capital.one.home.directs.dto.InfrastructureDTO;
import com.capital.one.home.directs.dto.InternalEUXDto;
import com.capital.one.home.directs.dto.OpusTransactionDto;
import com.capital.one.home.directs.entity.Transactions;
import com.capital.one.home.directs.response.OpusResponse;
import com.capital.one.home.directs.services.CassandraOpusService;
import com.capital.one.home.directs.services.OpusServices;
import com.capital.one.home.directs.utility.OpusException;

@Controller
@RequestMapping("/opus")
@PropertySource("classpath:config/application.properties")
public class OpusController {

	@Autowired
	OpusServices opusServices;
	
	@Autowired
	private CassandraOpusService cassandraOpusService;
	
	@Autowired
	private Environment env;
	
	
	private static final Logger logger = Logger.getLogger(OpusController.class);
	@RequestMapping(value="/transactions",method=RequestMethod.GET)
	@ResponseBody public OpusResponse getTransactionDetails() throws OpusException{
		
		logger.info("Start of Transaction Request");
		OpusResponse response = new OpusResponse();
		List<OpusTransactionDto> data = null;
		try{
			data = opusServices.getTransactionsData(env.getProperty("transaction"));
			response.setStatusCode(200);
			response.setStatusMessage("Transaction Response send successfully");
			response.setData(data);
			logger.info("Sending response for Transaction with status code : "+response.getStatusCode());
		}catch(OpusException oe){
			response.setStatusCode(oe.getStatusCode());
			response.setStatusMessage(oe.getMessage());
		}catch(Exception e){
			response.setStatusCode(400);
			response.setStatusMessage("Internal Server Error");
		}
		return response;
	}
	
	@RequestMapping(value="/infrastructure",method=RequestMethod.GET)
	@ResponseBody public OpusResponse getInfrastructureDetails() throws OpusException{
		
		logger.info("Start of infrastructure Request");
		List<InfrastructureDTO> data = null;
		OpusResponse response = new OpusResponse();
		try{
			data = opusServices.getInfrastructureData(env.getProperty("infrastructure"));
			response.setStatusCode(200);
			response.setStatusMessage("Infrastructure Response send successfully");
			response.setData(data);
			logger.info("Sending response for Infrastructure with status code : "+response.getStatusCode());
		}catch(OpusException oe){
			response.setStatusCode(oe.getStatusCode());
			response.setStatusMessage(oe.getMessage());
		}catch(Exception e){
			response.setStatusCode(400);
			response.setStatusMessage("Internal Server Error");
		}
		return response;
	}
	
	@RequestMapping(value="/eux",method=RequestMethod.GET)
	@ResponseBody public OpusResponse getInternalEuxDetails(HttpServletResponse servletResponse) throws OpusException{
		
		logger.info("Start of eux Request");
		List<InternalEUXDto> data = null;
		OpusResponse response = new OpusResponse();
		try{
			data = opusServices.getInternalEUXData(env.getProperty("eux"));
			response.setStatusCode(200);
			response.setStatusMessage("Internal EUX Response send successfully");
			response.setData(data);
			logger.info("Sending response for EuX with status code : "+response.getStatusCode());
		}catch(OpusException oe){
			response.setStatusCode(oe.getStatusCode());
			response.setStatusMessage(oe.getMessage());
		}catch(Exception e){
			response.setStatusCode(400);
			response.setStatusMessage("Internal Server Error");
		}
		return response;
	}
	
	@RequestMapping(value="/transactionsc",method=RequestMethod.GET)
	@ResponseBody public OpusResponse getTransactionDetailsFromDb(){
		
		logger.info("Start of Transaction Request");
		 List<Transactions> data = cassandraOpusService.getTransactionsDataFromDb(Integer.parseInt(env.getProperty("time")));
		OpusResponse response = new OpusResponse();
		response.setStatusCode(200);
		response.setData(data);
		response.setStatusMessage("Transaction Response send successfully");
		logger.info("Sending response for Transaction with status code : "+response.getStatusCode());
		return response;
	}
	
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
