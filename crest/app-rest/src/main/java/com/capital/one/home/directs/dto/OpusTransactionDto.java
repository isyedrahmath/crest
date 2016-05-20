package com.capital.one.home.directs.dto;

public class OpusTransactionDto {

	private Long actualVolume;
	private Long expectedVolume;
	private Long actualResponseTime;
	private Long expectedResponseTime;
	private String name;
	
	
	public Long getActualVolume() {
		return actualVolume;
	}
	public void setActualVolume(Long actualVolume) {
		this.actualVolume = actualVolume;
	}
	public Long getExpectedVolume() {
		return expectedVolume;
	}
	public void setExpectedVolume(Long expectedVolume) {
		this.expectedVolume = expectedVolume;
	}
	public Long getActualResponseTime() {
		return actualResponseTime;
	}
	public void setActualResponseTime(Long actualResponseTime) {
		this.actualResponseTime = actualResponseTime;
	}
	public Long getExpectedResponseTime() {
		return expectedResponseTime;
	}
	public void setExpectedResponseTime(Long expectedResponseTime) {
		this.expectedResponseTime = expectedResponseTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
