package com.capital.one.home.directs.response;

public class TxnResponse {

	private String txnName;
	private Double txnResponse;
	private Double txnVolume;
	private String timeStamp;
	
	public String getTxnName() {
		return txnName;
	}
	public void setTxnName(String txnName) {
		this.txnName = txnName;
	}
	public Double getTxnResponse() {
		return txnResponse;
	}
	public void setTxnResponse(Double txnResponse) {
		this.txnResponse = txnResponse;
	}
	public Double getTxnVolume() {
		return txnVolume;
	}
	public void setTxnVolume(Double txnVolume) {
		this.txnVolume = txnVolume;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
