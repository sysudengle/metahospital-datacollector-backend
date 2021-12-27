package com.metahospital.datacollector.dao.data;

public class UserDoctor {
	private long userId;
	private int hospitalId;
	private String staffId;
	private int status;
	private String departmentIds;

	public UserDoctor() {
	}

	public UserDoctor(long userId, int hospitalId, String staffId, int status, String departmentIds) {
		this.userId = userId;
		this.hospitalId = hospitalId;
		this.staffId = staffId;
		this.status = status;
		this.departmentIds = departmentIds;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}
}