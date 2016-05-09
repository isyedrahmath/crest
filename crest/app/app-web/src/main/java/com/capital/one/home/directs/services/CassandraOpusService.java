package com.capital.one.home.directs.services;

import java.util.Date;
import java.util.List;

import com.capital.one.home.directs.entity.Transactions;
import com.datastax.driver.core.Row;

public interface CassandraOpusService {

	List<Transactions> getTransactionsDataFromDb(int time);
	
	Date getDBcurrentTime();
}
