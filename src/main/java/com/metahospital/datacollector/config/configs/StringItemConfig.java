package com.metahospital.datacollector.config.configs;

import com.alibaba.fastjson.TypeReference;
import com.metahospital.datacollector.config.ConfigService;
import com.metahospital.datacollector.config.ConfigUtil;
import com.metahospital.datacollector.config.IConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringItemConfig implements IConfig {
    public static StringItemConfig get() {
        return ConfigService.getConfig(StringItemConfig.class);
    }

	private List<StringItemConfigData> dataList;
	private Map<Integer, StringItemConfigData> dataMap;

	public StringItemConfig() {
	    loadJson();
	}

    @Override
    public void loadJson() {
        this.dataList = ConfigUtil.loadDataList(StringItemConfig.class, new TypeReference<List<StringItemConfigData>>(){}.getType());
        this.dataMap = new HashMap<>();
        for (StringItemConfigData configData : dataList) {
            dataMap.put(configData.getStringItemTypeId(), configData);
        }
	}
	
	public List<StringItemConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, StringItemConfigData> getDataMap() {
		return dataMap;
	}

	public StringItemConfigData get(int stringItemTypeId) {
		return dataMap.get(stringItemTypeId);
	}
}
