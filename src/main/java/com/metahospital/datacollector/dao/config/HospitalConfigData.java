package com.metahospital.datacollector.dao.config;

import org.springframework.stereotype.Component;

/**
 * Created on 2021/12/28.
 */
@Component
public class HospitalConfigData {
	private int hospitalId;
	private String hospitalName;

	public HospitalConfigData() {
	}

	public HospitalConfigData(int hospitalId, String hospitalName) {
		this.hospitalId = hospitalId;
		this.hospitalName = hospitalName;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
}
