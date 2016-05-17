package com.capital.one.home.directs.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capital.one.home.directs.entity.InstantTxn;
import com.capital.one.home.directs.entity.InstantTxnHistory;
import com.capital.one.home.directs.repository.InstantTxnHistoryRepository;
import com.capital.one.home.directs.repository.InstantTxnRepository;
import com.capital.one.home.directs.response.InstantTxnResponse;
import com.capital.one.home.directs.response.TxnResponse;
import com.capital.one.home.directs.services.OpusServices;
import com.datastax.driver.core.Row;

@Service
public class OpusServicesImpl implements OpusServices {

	@Autowired
	private InstantTxnRepository instantTxnRepository;
	@Autowired
	private InstantTxnHistoryRepository instantTxnHistoryRepository;

	private static final Logger logger = Logger.getLogger(OpusServicesImpl.class);
	

	@Override
	public Map<String,ArrayList<TxnResponse>> getAllInstantTxnDetails() {
		
		Map<String,ArrayList<InstantTxnResponse>> response = new HashMap<String, ArrayList<InstantTxnResponse>>();
		Map<String,ArrayList<TxnResponse>> txnResponse = new HashMap<String, ArrayList<TxnResponse>>();
		Iterable<InstantTxn> instantList = instantTxnRepository.getInstantTxnDetails();
		instantList = null;
		if(instantList == null || instantList.iterator() == null  || instantList.iterator().hasNext() == false){
			Date currentDateTime = getDBcurrentTime();
			 String startDateTime = convertDateToString(substractNMinsFromDate(currentDateTime,-5),"yyyy-MM-dd HH:mm:ss");
			 String endDateTime = convertDateToString(currentDateTime,"yyyy-MM-dd HH:mm:ss");
			 List<InstantTxn> txnHistoryList = new ArrayList<InstantTxn>();
			 Iterable<InstantTxnHistory> resultSet = instantTxnHistoryRepository.getTransactionsDetialsFromDb(startDateTime,endDateTime);
			 for(InstantTxnHistory transaction : resultSet){
				 InstantTxn instantTxn = new InstantTxn();
				 BeanUtils.copyProperties(transaction, instantTxn);
				 instantTxn.setTimeStamp(endDateTime);
				 txnHistoryList.add(instantTxn);
			 }
			 instantList = txnHistoryList;
		}
		
		for(InstantTxn instantTxn : instantList){
			response.put(instantTxn.getTrxnname(), new ArrayList<InstantTxnResponse>());
			txnResponse.put(instantTxn.getServername(), new ArrayList<TxnResponse>());
		}
		for(InstantTxn instantTxn : instantList){
			
			InstantTxnResponse txnResp = new InstantTxnResponse();
			txnResp.setResponseTime(instantTxn.getTrxnresponsetime());
			txnResp.setVolume(instantTxn.getTrxnvolume());
			txnResp.setServerName(instantTxn.getServername());
			txnResp.setTimeStamp(instantTxn.getTimeStamp());
			response.get(instantTxn.getTrxnname()).add(txnResp);
		}
		
		Set<String> txnNames = response.keySet();
		for(String txnName : txnNames) {
			List<InstantTxnResponse> txnDetails = response.get(txnName);
			Double responseTime_ =0.0;
			Double volume_ = 0.0;
			String serverName_=null;
			String timestamp = null;
			for(InstantTxnResponse tResponse : txnDetails){
				responseTime_+=tResponse.getResponseTime();
				volume_+=tResponse.getVolume();
				serverName_=tResponse.getServerName();
				timestamp = tResponse.getTimeStamp();
			}
			responseTime_ = responseTime_/txnDetails.size();
			TxnResponse txnResponse_ = new TxnResponse();
			txnResponse_.setTxnName(txnName);
			txnResponse_.setTxnResponse(responseTime_);
			txnResponse_.setTxnVolume(volume_);
			txnResponse_.setTimeStamp(timestamp);
			txnResponse.get(serverName_).add(txnResponse_);
		}
		
		return txnResponse;
		
	}

	@Override
	public Map<String,ArrayList<TxnResponse>> getServerSpecificInstantTxnDetails(String servername) {
		
		Map<String,ArrayList<InstantTxnResponse>> response = new HashMap<String, ArrayList<InstantTxnResponse>>();
		Map<String,ArrayList<TxnResponse>> txnResponse = new HashMap<String, ArrayList<TxnResponse>>();
		Iterable<InstantTxn> instantList = instantTxnRepository.getInstantTxnDetailsByServername(servername);
		for(InstantTxn instantTxn : instantList){
			response.put(instantTxn.getTrxnname(), new ArrayList<InstantTxnResponse>());
			txnResponse.put(instantTxn.getServername(), new ArrayList<TxnResponse>());
		}
		for(InstantTxn instantTxn : instantList){
			
			InstantTxnResponse txnResp = new InstantTxnResponse();
			txnResp.setResponseTime(instantTxn.getTrxnresponsetime());
			txnResp.setVolume(instantTxn.getTrxnvolume());
			txnResp.setServerName(instantTxn.getServername());
			response.get(instantTxn.getTrxnname()).add(txnResp);
		}
		
		Set<String> txnNames = response.keySet();
		for(String txnName : txnNames) {
			List<InstantTxnResponse> txnDetails = response.get(txnName);
			Double responseTime_ =0.0;
			Double volume_ = 0.0;
			String serverName_=null;
			for(InstantTxnResponse tResponse : txnDetails){
				responseTime_+=tResponse.getResponseTime();
				volume_+=tResponse.getVolume();
				serverName_=tResponse.getServerName();
			}
			responseTime_ = responseTime_/txnDetails.size();
			TxnResponse txnResponse_ = new TxnResponse();
			txnResponse_.setTxnName(txnName);
			txnResponse_.setTxnResponse(responseTime_);
			txnResponse_.setTxnVolume(volume_);
			txnResponse.get(serverName_).add(txnResponse_);
		}
		
		return txnResponse;
	}
	
	public Date getDBcurrentTime() {
		Iterable<Row> result = instantTxnHistoryRepository.getDBcurrentTime();
		Row row = result.iterator().next();
		Date date = row.getDate(0);
		return date;
	}
	
	private String convertDateToString(Date date,String format){
		
		Format formatter = new SimpleDateFormat(format);
		String strDate = formatter.format(date);
		return strDate;
	}
	
	private Date substractNMinsFromDate(Date date,int mins){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, mins);
		return cal.getTime();
	}


}
