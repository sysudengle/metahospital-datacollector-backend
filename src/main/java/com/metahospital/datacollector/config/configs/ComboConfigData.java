package com.metahospital.datacollector.config.configs;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 2021/12/28.
 */
public class ComboConfigData {
	private int comboId;
	private String comboName;
	private List<Integer> itemIds;

	public ComboConfigData() {
	}

	public ComboConfigData(int comboId, String comboName, List<Integer> itemIds) {
		this.comboId = comboId;
		this.comboName = comboName;
		this.itemIds = itemIds;
	}

	public int getComboId() {
		return comboId;
	}

	public String getComboName() {
		return comboName;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}
}
