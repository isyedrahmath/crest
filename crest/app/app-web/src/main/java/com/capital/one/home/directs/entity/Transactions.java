package com.capital.one.home.directs.entity;

import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("transactions")
public class Transactions {
	
	@PrimaryKey
	@Column("tid")
	private Integer tid;
	@Column("name")
	private String name;
	@Column("volume")
	private Integer volume;
	@Column("response")
	private Double response;
	@Column("time_")
	private Date time;
	
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public Double getResponse() {
		return response;
	}
	public void setResponse(Double response) {
		this.response = response;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
	
}
