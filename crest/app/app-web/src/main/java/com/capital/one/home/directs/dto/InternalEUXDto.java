package com.capital.one.home.directs.dto;

public class InternalEUXDto {
	
	private String name;
	private Long actualVolume;
	private Long actualResponseTime;
	private Long expectedResponseTime1;
	private Long expectedResponseTime2;
	private Long expectedVolume;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getActualVolume() {
		return actualVolume;
	}
	public void setActualVolume(Long actualVolume) {
		this.actualVolume = actualVolume;
	}
	public Long getActualResponseTime() {
		return actualResponseTime;
	}
	public void setActualResponseTime(Long actualResponseTime) {
		this.actualResponseTime = actualResponseTime;
	}
	public Long getExpectedResponseTime1() {
		return expectedResponseTime1;
	}
	public void setExpectedResponseTime1(Long expectedResponseTime1) {
		this.expectedResponseTime1 = expectedResponseTime1;
	}
	public Long getExpectedResponseTime2() {
		return expectedResponseTime2;
	}
	public void setExpectedResponseTime2(Long expectedResponseTime2) {
		this.expectedResponseTime2 = expectedResponseTime2;
	}
	public Long getExpectedVolume() {
		return expectedVolume;
	}
	public void setExpectedVolume(Long expectedVolume) {
		this.expectedVolume = expectedVolume;
	}
	
	

}
