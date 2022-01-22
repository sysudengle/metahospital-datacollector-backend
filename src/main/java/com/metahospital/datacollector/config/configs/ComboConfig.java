package com.metahospital.datacollector.config.configs;

import com.alibaba.fastjson.TypeReference;
import com.metahospital.datacollector.config.ConfigService;
import com.metahospital.datacollector.config.ConfigUtil;
import com.metahospital.datacollector.config.IConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2021/12/28.
 */
public class ComboConfig implements IConfig {
    public static ComboConfig get() {
        return ConfigService.getConfig(ComboConfig.class);
    }

	private List<ComboConfigData> dataList;
	private Map<Integer, ComboConfigData> dataMap;

	public ComboConfig() {
	}

    @Override
    public void loadJson() {
        this.dataList = ConfigUtil.loadDataList(ComboConfig.class, new TypeReference<List<ComboConfigData>>(){}.getType());
        this.dataMap = new HashMap<>();
        for (ComboConfigData configData : dataList) {
            dataMap.put(configData.getComboId(), configData);
        }
	}
	
	public List<ComboConfigData> getDataList() {
		return dataList;
	}

    public Map<Integer, ComboConfigData> getDataMap() {
        return dataMap;
    }

    public ComboConfigData get(int comboId) {
        return dataMap.get(comboId);
	}
}
