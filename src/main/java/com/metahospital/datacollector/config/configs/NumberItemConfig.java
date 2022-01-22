package com.metahospital.datacollector.config.configs;

import com.alibaba.fastjson.TypeReference;
import com.metahospital.datacollector.config.ConfigService;
import com.metahospital.datacollector.config.ConfigUtil;
import com.metahospital.datacollector.config.IConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberItemConfig implements IConfig {
    public static NumberItemConfig get() {
        return ConfigService.getConfig(NumberItemConfig.class);
    }

	private List<NumberItemConfigData> dataList;
	private Map<Integer, NumberItemConfigData> dataMap;

	public NumberItemConfig() {
	    loadJson();
	}

    @Override
    public void loadJson() {
        this.dataList = ConfigUtil.loadDataList(NumberItemConfig.class, new TypeReference<List<NumberItemConfigData>>(){}.getType());
        this.dataMap = new HashMap<>();
        for (NumberItemConfigData configData : dataList) {
            dataMap.put(configData.getNumberItemTypeId(), configData);
        }
	}
	
	public List<NumberItemConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, NumberItemConfigData> getDataMap() {
		return dataMap;
	}

	public NumberItemConfigData get(int numberItemTypeId) {
		return dataMap.get(numberItemTypeId);
	}
}
