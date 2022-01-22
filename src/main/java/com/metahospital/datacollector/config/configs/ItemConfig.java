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
public class ItemConfig implements IConfig {
    public static ItemConfig get() {
        return ConfigService.getConfig(ItemConfig.class);
    }

	private List<ItemConfigData> dataList;
	private Map<Integer, ItemConfigData> dataMap;

	public ItemConfig() {
	    loadJson();
	}

    @Override
    public void loadJson() {
        this.dataList = ConfigUtil.loadDataList(ItemConfig.class, new TypeReference<List<ItemConfigData>>(){}.getType());
        this.dataMap = new HashMap<>();
        for (ItemConfigData configData : dataList) {
            dataMap.put(configData.getItemId(), configData);
        }
	}
	
	public List<ItemConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, ItemConfigData> getDataMap() {
		return dataMap;
	}

	public ItemConfigData get(int itemId) {
		return dataMap.get(itemId);
	}
}
