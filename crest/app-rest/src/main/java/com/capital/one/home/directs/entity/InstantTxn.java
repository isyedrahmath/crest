package com.capital.one.home.directs.entity;

import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("trxn_instant")
public class InstantTxn {

	@PrimaryKey
	@Column("servername")
	private String servername;
	@Column("trxnname")
	private String trxnname;
	@Column("datetime")
	private Date datetime;
	@Column("date")
	private Date date;
	@Column("trxnresponsetime")
	private Double trxnresponsetime;
	@Column("trxnvolume")
	private Double trxnvolume;
	private String timeStamp;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getServername() {
		return servername;
	}
	public void setServername(String servername) {
		this.servername = servername;
	}
	public String getTrxnname() {
		return trxnname;
	}
	public void setTrxnname(String trxnname) {
		this.trxnname = trxnname;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getTrxnresponsetime() {
		return trxnresponsetime;
	}
	public void setTrxnresponsetime(Double trxnresponsetime) {
		this.trxnresponsetime = trxnresponsetime;
	}
	public Double getTrxnvolume() {
		return trxnvolume;
	}
	public void setTrxnvolume(Double trxnvolume) {
		this.trxnvolume = trxnvolume;
	}
	
	
}
