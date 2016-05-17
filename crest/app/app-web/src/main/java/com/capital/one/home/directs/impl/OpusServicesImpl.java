package com.capital.one.home.directs.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capital.one.home.directs.entity.InstantTxn;
import com.capital.one.home.directs.repository.InstantTxnRepository;
import com.capital.one.home.directs.response.InstantTxnResponse;
import com.capital.one.home.directs.response.TxnResponse;
import com.capital.one.home.directs.services.OpusServices;

@Service
public class OpusServicesImpl implements OpusServices {

	@Autowired
	private InstantTxnRepository instantTxnRepository;

	private static final Logger logger = Logger.getLogger(OpusServicesImpl.class);
	

	@Override
	public Map<String,ArrayList<TxnResponse>> getAllInstantTxnDetails() {
		
		Map<String,ArrayList<InstantTxnResponse>> response = new HashMap<String, ArrayList<InstantTxnResponse>>();
		Map<String,ArrayList<TxnResponse>> txnResponse = new HashMap<String, ArrayList<TxnResponse>>();
		Iterable<InstantTxn> instantList = instantTxnRepository.getInstantTxnDetails();
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


}
