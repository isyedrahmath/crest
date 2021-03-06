package com.capital.one.home.directs.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.capital.one.home.directs.dto.InfrastructureDTO;
import com.capital.one.home.directs.dto.InternalEUXDto;
import com.capital.one.home.directs.dto.OpusTransactionDto;
import com.capital.one.home.directs.response.InstantTxnResponse;
import com.capital.one.home.directs.response.TxnResponse;
import com.capital.one.home.directs.utility.OpusException;
import com.datastax.driver.core.Row;

public interface OpusServices {
	
	Map<String,ArrayList<TxnResponse>> getAllInstantTxnDetails();
	Map<String,ArrayList<TxnResponse>> getServerSpecificInstantTxnDetails(String servername);
	
}
