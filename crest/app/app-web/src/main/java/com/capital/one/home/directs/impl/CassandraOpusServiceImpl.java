package com.capital.one.home.directs.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capital.one.home.directs.entity.Transactions;
import com.capital.one.home.directs.services.CassandraOpusService;
import com.capital.one.home.directs.repository.TransactionRepository;
import com.datastax.driver.core.Row;

@Service
public class CassandraOpusServiceImpl implements CassandraOpusService{

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	public List<Transactions> getTransactionsDataFromDb(int time) {
		List<Transactions> result = new ArrayList<Transactions>();
		Date currentDateTime = getDBcurrentTime();
		String startDateTime = convertDateToString(substractNMinsFromDate(currentDateTime,-time),"yyyy-MM-dd HH:mm:ss");
		String endDateTime = convertDateToString(currentDateTime,"yyyy-MM-dd HH:mm:ss");
		Iterable<Transactions> resultSet = transactionRepository.getTransactionsDetialsFromDb(startDateTime,endDateTime);
		for(Transactions transaction : resultSet){
			result.add(transaction);
		}
		return result;
	}

	@Override
	public Date getDBcurrentTime() {
		Iterable<Row> result = transactionRepository.getDBcurrentTime();
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
