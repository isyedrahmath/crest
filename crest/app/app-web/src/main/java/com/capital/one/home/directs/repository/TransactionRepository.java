package com.capital.one.home.directs.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.capital.one.home.directs.entity.Transactions;
import com.datastax.driver.core.Row;

public interface TransactionRepository  extends CassandraRepository<Transactions>{

	@Query("SELECT dateof(now()) FROM system.local")
	Iterable<Row> getDBcurrentTime();
	
	@Query("select * from monitoring.transactions where time_ >= ?0 and time_ <= ?1 ALLOW FILTERING; ")
	Iterable<Transactions> getTransactionsDetialsFromDb(String startDateTime,String endDateTime);
}