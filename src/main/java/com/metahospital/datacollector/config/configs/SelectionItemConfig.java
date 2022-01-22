package com.metahospital.datacollector.config.configs;

import com.alibaba.fastjson.TypeReference;
import com.metahospital.datacollector.config.ConfigService;
import com.metahospital.datacollector.config.ConfigUtil;
import com.metahospital.datacollector.config.IConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectionItemConfig implements IConfig {
    public static final int SELECT_NUM_MULTI = 0;

    public static SelectionItemConfig get() {
        return ConfigService.getConfig(SelectionItemConfig.class);
    }

	private List<SelectionItemConfigData> dataList;
	private Map<Integer, SelectionItemConfigData> dataMap;

	public SelectionItemConfig() {
	    loadJson();
	}

    @Override
    public void loadJson() {
        this.dataList = ConfigUtil.loadDataList(SelectionItemConfig.class, new TypeReference<List<SelectionItemConfigData>>(){}.getType());
        this.dataMap = new HashMap<>();
        for (SelectionItemConfigData configData : dataList) {
            dataMap.put(configData.getSelectionItemTypeId(), configData);
        }
	}
	
	public List<SelectionItemConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, SelectionItemConfigData> getDataMap() {
		return dataMap;
	}

	public SelectionItemConfigData get(int selectionItemTypeId) {
		return dataMap.get(selectionItemTypeId);
	}
}
