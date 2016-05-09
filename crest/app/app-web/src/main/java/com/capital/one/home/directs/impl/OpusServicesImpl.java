package com.capital.one.home.directs.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capital.one.home.directs.dto.InfrastructureDTO;
import com.capital.one.home.directs.dto.InternalEUXDto;
import com.capital.one.home.directs.dto.OpusTransactionDto;
import com.capital.one.home.directs.entity.InstantTxn;
import com.capital.one.home.directs.repository.InstantTxnRepository;
import com.capital.one.home.directs.repository.TransactionRepository;
import com.capital.one.home.directs.response.InstantTxnResponse;
import com.capital.one.home.directs.response.TxnResponse;
import com.capital.one.home.directs.services.OpusServices;
import com.capital.one.home.directs.utility.OpusException;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class OpusServicesImpl implements OpusServices {

	@Autowired 
	private TransactionRepository transactionRepository;
	@Autowired
	private InstantTxnRepository instantTxnRepository;

	private static final Logger logger = Logger.getLogger(OpusServicesImpl.class);
	
	public List<OpusTransactionDto> getTransactionsData(String csvFileName) throws OpusException{
		
		List<OpusTransactionDto> transactionResponseList = new ArrayList<OpusTransactionDto>();
		try
        {
			logger.info("Start of fetching transaction data from file : "+csvFileName);
			CSVReader reader = new CSVReader(new FileReader(csvFileName));
		    List transactionData = reader.readAll();
		    Map<String,Integer> serverNames = new HashMap<String,Integer>();
		    for(int i=1;i<transactionData.size();i++){
		    	String [] transactionRow = (String[]) transactionData.get(i); 
		    	serverNames.put(transactionRow[3], i);
		    }
		    logger.info("No.of Records found in file "+csvFileName+" are "+transactionData.size() );
		    Set<String> names = serverNames.keySet();
		    for(String name : names){
		    	
		    	String[] transactionRow = (String[])transactionData.get(serverNames.get(name));
		    	OpusTransactionDto opusTransaction = new OpusTransactionDto();
		    	opusTransaction.setName(name);
		    	opusTransaction.setActualResponseTime((long)((transactionRow[1]!=null)?roundDouble(Double.valueOf(transactionRow[1]),2):0.0d));
		    	opusTransaction.setActualVolume((long)((transactionRow[2]!=null)?roundDouble(Double.valueOf(transactionRow[2]),2):0.0d));
		    	transactionResponseList.add(opusTransaction);
		    }
            
        }catch(IOException io){
        	logger.error("Exception occured : "+io.getMessage());
        	throw new OpusException(401, "Transaction Csv File not found");
		} catch (Exception e)
        {
			logger.error("Exception occured : "+e.getMessage());
			throw new OpusException(400, "Internal Server Error");
        }
		return transactionResponseList;
	}

	public List<InfrastructureDTO> getInfrastructureData(String fileName) throws OpusException {
		
		List<InfrastructureDTO> infraStructureDetails = null;
		try{
			
			infraStructureDetails = new ArrayList<InfrastructureDTO>();
			FileInputStream file = new FileInputStream(new File(fileName));
			 
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //HSSFWorkbook workbook = new HSSFWorkbook(file);
 
            XSSFSheet sheet = workbook.getSheetAt(0);
            //HSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNumber = sheet.getLastRowNum();
            Iterator<Row> rowIterator = sheet.iterator();
            Map<String,Integer> serverNames = new HashMap<String,Integer>();
            boolean firstRowFlag = true;
            int rowIndex=1;
            while (rowIterator.hasNext()){
            	
            	Row row = rowIterator.next();
            	if(firstRowFlag){
            		firstRowFlag = false;
            		continue;
            	}
            	Iterator<Cell> cellIterator = row.cellIterator();
            	int cellIndex=1;
            	while (cellIterator.hasNext()){
            		
            		Cell cell = cellIterator.next();
            		if(cellIndex ==1 )
            			serverNames.put(cell.getStringCellValue(), rowIndex);
            		break;
            		
            	}
            	rowIndex++;
            }
            List<Integer> rowNumbers = new ArrayList<Integer>();
            Set<String> names = serverNames.keySet();
            for(String name : names){
            	rowNumbers.add(serverNames.get(name));
            }
            
            rowIterator = sheet.iterator();
            int rowCounter = 1;
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                if(rowNumbers.contains(rowCounter)){
                	
                	InfrastructureDTO infrastructure = new InfrastructureDTO();
                	Iterator<Cell> cellIterator = row.cellIterator();
                    int cellIndex = 1;
                    while (cellIterator.hasNext())
                    {
                        Cell cell = cellIterator.next();
                        switch(cellIndex)
                        {
                        case 1:
                        	infrastructure.setName(cell.getStringCellValue());
                        	break;
                        case 3: 
                        	infrastructure.setActualCpuUtilization((long)((cell.getNumericCellValue()>0)?roundDouble(Double.valueOf(cell.getNumericCellValue()),2):0.0d));
                        	break;
                        case 5:
                        	infrastructure.setActualMemoryUtilization((long)((cell.getNumericCellValue()>0)?roundDouble(Double.valueOf(cell.getNumericCellValue()),2):0.0d));
                        	break;
                        case 7:
                        	infrastructure.setActualIODisk((long)((cell.getNumericCellValue()>0)?roundDouble(Double.valueOf(cell.getNumericCellValue()),2):0.0d));
                        	break;
                        	
                        }
                        cellIndex++;
                    }
                    infraStructureDetails.add(infrastructure);
                }
                rowCounter++;
                
            }
            file.close();
			
		}catch(IOException io){
        	logger.error("Exception occured : "+io.getMessage());
        	throw new OpusException(402, "Infrastructure file not found");
		} catch (Exception e)
        {
			logger.error("Exception occured : "+e.getMessage());
			throw new OpusException(400, "Internal Server Error");
        }
		return infraStructureDetails;
	}

	@Override
	public List<InternalEUXDto> getInternalEUXData(String fileName) throws OpusException {

		List<InternalEUXDto> euxData = null;
		try{
			
			euxData = new ArrayList<InternalEUXDto>();
			FileInputStream file = new FileInputStream(new File(fileName));
			 
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //HSSFWorkbook workbook = new HSSFWorkbook(file);
 
            XSSFSheet sheet = workbook.getSheetAt(0);
            //HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Map<String,String> serverNames = new HashMap<String,String>();
            boolean firstRowFlag = true;
            int rowIndex=1;
            while (rowIterator.hasNext()){
            	
            	Row row = rowIterator.next();
            	if(firstRowFlag){
            		firstRowFlag = false;
            		continue;
            	}
            	Iterator<Cell> cellIterator = row.cellIterator();
            	int cellIndex=1;
            	String name=null;
            	while (cellIterator.hasNext()){
            		
            		Cell cell = cellIterator.next();
            		switch(cellIndex)
                    {
	                    case 3:
	                    	name=cell.getStringCellValue();
	                    	if(!serverNames.containsKey(name))
	                    		serverNames.put(name, 0.0+":"+0.0+":"+1+":"+0+":"+0+":"+0);
	                    	else{
	                    		String[] values = serverNames.get(name).split(":");
	                    		Double value = Double.parseDouble(values[0]);
	                    		Double volume = Double.parseDouble(values[1]);
	                    		int rowCounter = Integer.parseInt(values[2]);
	                    		rowCounter+=1;//rowCounter = rowCounter+1;
	                    		Long ert1 = Long.parseLong(values[3]);
	                    		Long ert2 = Long.parseLong(values[4]);
	                    		Long ev = Long.parseLong(values[5]);
	                    		serverNames.put(name, value+":"+volume+":"+rowCounter+":"+ert1+":"+ert2+":"+ev);
	                    	}
	                    	break;
	                    case 7:
	                    {
	                    	String[] values = serverNames.get(name).split(":");
	                    	Double val = cell.getNumericCellValue();
	                    	Double value = Double.parseDouble(values[0])+val;
	                    	Long ert1 = Long.parseLong(values[3]);
	                    	Long ert2 = Long.parseLong(values[4]);
	                    	Long ev = Long.parseLong(values[5]);
	                    	serverNames.put(name, value+":"+values[1]+":"+values[2]+":"+ert1+":"+ert2+":"+ev);
	                    }
	                    	break;
	                    case 8:
	                    {
	                    	String[] volumes = serverNames.get(name).split(":");
	                    	Double vol = cell.getNumericCellValue();
	                    	Double volume = Double.parseDouble(volumes[1])+vol;
	                    	Long ert1 = Long.parseLong(volumes[3]);
	                    	Long ert2 = Long.parseLong(volumes[4]);
	                    	Long ev = Long.parseLong(volumes[5]);
	                    	serverNames.put(name, volumes[0]+":"+volume+":"+volumes[2]+":"+ert1+":"+ert2+":"+ev);
	                    }
	                    	break;
	                    case 17:
	                    {
	                    	String[] volumes = serverNames.get(name).split(":");
	                    	Double vol = cell.getNumericCellValue();
	                    	Integer ert1 = vol.intValue();
                    		Integer ert2 = Integer.parseInt(volumes[4]);
                    		Integer ev = Integer.parseInt(volumes[5]);
	                    	serverNames.put(name, volumes[0]+":"+volumes[1]+":"+volumes[2]+":"+ert1+":"+ert2+":"+ev);
	                    }
	                    	break;
	                    case 18:
	                    {
	                    	String[] volumes = serverNames.get(name).split(":");
	                    	Double vol = cell.getNumericCellValue();
	                    	Long ert1 = Long.parseLong(volumes[3]);
	                    	Long ert2 = vol.longValue();
	                    	Long ev = Long.parseLong(volumes[5]);
	                    	serverNames.put(name, volumes[0]+":"+volumes[1]+":"+volumes[2]+":"+ert1+":"+ert2+":"+ev);
	                    }
	                    	break;
	                    case 19:
	                    {
	                    	String[] volumes = serverNames.get(name).split(":");
	                    	Double vol = cell.getNumericCellValue();
	                    	Long ert1 = Long.parseLong(volumes[3]);
	                    	Long ert2 = Long.parseLong(volumes[4]);
	                    	Long ev = vol.longValue();
	                    	serverNames.put(name, volumes[0]+":"+volumes[1]+":"+volumes[2]+":"+ert1+":"+ert2+":"+ev);
	                    }
	                    	break;
                    	
                    }
                    cellIndex++;
            		
            	}
            	rowIndex++;
            }
            Set<String> euxNames = serverNames.keySet();
            for(String euxName : euxNames){
            	
            	String valueVolume = serverNames.get(euxName);
            	String[] valueVolumeArray = valueVolume.split(":");
            	int rowCounter = Integer.parseInt(valueVolumeArray[2]);
            	Long ert1 = Long.parseLong(valueVolumeArray[3]);
            	Long ert2 = Long.parseLong(valueVolumeArray[4]);
            	Long ev = Long.parseLong(valueVolumeArray[5]);
            	
            	Double value = Double.parseDouble(valueVolumeArray[0])/rowCounter;
            	Double volume = Double.parseDouble(valueVolumeArray[1])/rowCounter;
            	
            	InternalEUXDto internalEUXDto = new InternalEUXDto();
            	
            	internalEUXDto.setName(euxName);
            	internalEUXDto.setActualVolume((long) ((volume!=null)?roundDouble(Double.valueOf(volume),2):0.0));
            	internalEUXDto.setActualResponseTime((long)((value!=null)?roundDouble(Double.valueOf(value),2):0.0));
            	internalEUXDto.setExpectedResponseTime1(ert1);
            	internalEUXDto.setExpectedResponseTime2(ert2);
            	internalEUXDto.setExpectedVolume(ev);
            	
            	euxData.add(internalEUXDto);
            	
            }
			
		}catch(IOException io){
        	logger.error("Exception occured : "+io.getMessage());
        	throw new OpusException(403, "Internal EUX File not found");
		} catch (Exception e)
        {
			logger.error("Exception occured : "+e.getMessage());
			throw new OpusException(400, "Internal Server Error");
        }
		return euxData;
	}
	
	public static long roundDouble(Double value,int places){
		
		BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.longValue();
	}

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
