package com.capital.one.home.directs.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.capital.one.home.directs.entity.InstantTxn;


public interface InstantTxnRepository extends CassandraRepository<InstantTxn>{

	@Query("select * from monitoring.trxn_instant where servername=?0")
	Iterable<InstantTxn> getInstantTxnDetailsByServername(String servername);
	
	@Query("select * from monitoring.trxn_instant")
	Iterable<InstantTxn> getInstantTxnDetails();
}

