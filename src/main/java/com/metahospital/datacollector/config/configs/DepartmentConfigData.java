package com.metahospital.datacollector.config.configs;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 2021/12/28.
 */
public class DepartmentConfigData {
	private int departmentId;
	private String departmentName;
	private List<Integer> itemIds;

	public DepartmentConfigData() {
	}

	public DepartmentConfigData(int departmentId, String departmentName, List<Integer> itemIds) {
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.itemIds = itemIds;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}
}
