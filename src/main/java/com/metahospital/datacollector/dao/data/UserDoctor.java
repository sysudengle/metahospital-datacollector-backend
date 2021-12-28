package com.metahospital.datacollector.dao.data;

import com.metahospital.datacollector.common.enums.DoctorStatus;

public class UserDoctor {
	private long userId;
	private int hospitalId;
	private String staffId;
	private DoctorStatus status;
	private String departmentIds;

	public UserDoctor() {
	}

	public UserDoctor(long userId, int hospitalId, String staffId, DoctorStatus status, String departmentIds) {
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

	public void setStatus(DoctorStatus status) {
		this.status = status;
	}

	public String getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}
}