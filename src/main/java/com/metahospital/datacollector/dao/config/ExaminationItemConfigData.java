package com.metahospital.datacollector.dao.config;

import org.springframework.stereotype.Component;

/**
 * Created on 2021/12/28.
 */
@Component
public class ExaminationItemConfigData {
	private int examinationItemId;
	private String examinationItemName;

	public ExaminationItemConfigData() {
	}

	public ExaminationItemConfigData(int examinationItemId, String examinationItemName) {
		this.examinationItemId = examinationItemId;
		this.examinationItemName = examinationItemName;
	}

	public int getExaminationItemId() {
		return examinationItemId;
	}

	public String getExaminationItemName() {
		return examinationItemName;
	}
}
