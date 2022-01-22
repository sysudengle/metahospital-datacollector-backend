package com.metahospital.datacollector.config.configs;

import com.metahospital.datacollector.config.configs.customtype.SelectionItemOptionCO;

import java.util.List;

public class SelectionItemConfigData {
	private int selectionItemTypeId;
    private List<Integer> defaultValue;
    private List<SelectionItemOptionCO> optionList;
	private int selectNum;

	public SelectionItemConfigData() {
	}

    public SelectionItemConfigData(int selectionItemTypeId, List<Integer> defaultValue, List<SelectionItemOptionCO> optionList, int selectNum) {
        this.selectionItemTypeId = selectionItemTypeId;
        this.defaultValue = defaultValue;
        this.optionList = optionList;
        this.selectNum = selectNum;
    }

    public int getSelectionItemTypeId() {
        return selectionItemTypeId;
    }

    public List<Integer> getDefaultValue() {
        return defaultValue;
    }

    public List<SelectionItemOptionCO> getOptionList() {
        return optionList;
    }

    public int getSelectNum() {
        return selectNum;
    }
}
