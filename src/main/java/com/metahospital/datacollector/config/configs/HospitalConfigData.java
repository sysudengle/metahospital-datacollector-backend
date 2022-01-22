package com.metahospital.datacollector.config.configs;

import java.util.List;

/**
 * Created on 2021/12/28.
 */
public class HospitalConfigData {
	private int hospitalId;
	private String hospitalName;
	private List<Integer> departmentIds;
    private List<Integer> comboIds;

	public HospitalConfigData() {
	}

	public HospitalConfigData(int hospitalId, String hospitalName, List<Integer> departmentIds, List<Integer> comboIds) {
		this.hospitalId = hospitalId;
		this.hospitalName = hospitalName;
		this.departmentIds = departmentIds;
		this.comboIds = comboIds;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

    public List<Integer> getDepartmentIds() {
        return departmentIds;
    }

    public List<Integer> getComboIds() {
        return comboIds;
    }
}
