package com.capital.one.home.directs.dto;

public class InfrastructureDTO {

	private String name;
	private Long actualCpuUtilization;
	private Long expectedCpuUtilization;
	private Long actualMemoryUtilization;
	private Long expectedMemoryUtilization;
	private Long actualIODisk;
	private Long expectedIODisk;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getActualCpuUtilization() {
		return actualCpuUtilization;
	}
	public void setActualCpuUtilization(Long actualCpuUtilization) {
		this.actualCpuUtilization = actualCpuUtilization;
	}
	public Long getExpectedCpuUtilization() {
		return expectedCpuUtilization;
	}
	public void setExpectedCpuUtilization(Long expectedCpuUtilization) {
		this.expectedCpuUtilization = expectedCpuUtilization;
	}
	public Long getActualMemoryUtilization() {
		return actualMemoryUtilization;
	}
	public void setActualMemoryUtilization(Long actualMemoryUtilization) {
		this.actualMemoryUtilization = actualMemoryUtilization;
	}
	public Long getExpectedMemoryUtilization() {
		return expectedMemoryUtilization;
	}
	public void setExpectedMemoryUtilization(Long expectedMemoryUtilization) {
		this.expectedMemoryUtilization = expectedMemoryUtilization;
	}
	public Long getActualIODisk() {
		return actualIODisk;
	}
	public void setActualIODisk(Long actualIODisk) {
		this.actualIODisk = actualIODisk;
	}
	public Long getExpectedIODisk() {
		return expectedIODisk;
	}
	public void setExpectedIODisk(Long expectedIODisk) {
		this.expectedIODisk = expectedIODisk;
	}
	
	
}
