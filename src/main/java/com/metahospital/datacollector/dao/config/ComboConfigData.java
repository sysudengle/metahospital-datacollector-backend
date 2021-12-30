package com.metahospital.datacollector.dao.config;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 2021/12/28.
 */
@Component
public class ComboConfigData {
	private int hospitalId;
	private int comboId;
	private String comboName;
	private List<Integer> examinationItemIds;

	public ComboConfigData() {
	}

	public ComboConfigData(int hospitalId, int comboId, String comboName, List<Integer> examinationItemIds) {
		this.hospitalId = hospitalId;
		this.comboId = comboId;
		this.comboName = comboName;
		this.examinationItemIds = examinationItemIds;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public int getComboId() {
		return comboId;
	}

	public String getComboName() {
		return comboName;
	}

	public List<Integer> getExaminationItemIds() {
		return examinationItemIds;
	}
}
