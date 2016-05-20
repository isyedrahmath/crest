package com.capital.one.home.directs.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.capital.one.home.directs.entity.InstantTxnHistory;
import com.datastax.driver.core.Row;

public interface InstantTxnHistoryRepository extends CassandraRepository<InstantTxnHistory>{

	@Query("SELECT dateof(now()) FROM system.local")
	Iterable<Row> getDBcurrentTime();
	
	@Query("select * from monitoring.trxn_hist where datetime >= ?0 and datetime <= ?1 ALLOW FILTERING; ")
	Iterable<InstantTxnHistory> getTransactionsDetialsFromDb(String startDateTime,String endDateTime);
}
