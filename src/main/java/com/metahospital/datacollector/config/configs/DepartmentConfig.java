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
public class DepartmentConfig implements IConfig {
    public static DepartmentConfig get() {
        return ConfigService.getConfig(DepartmentConfig.class);
    }

	private List<DepartmentConfigData> dataList;
	private Map<Integer, DepartmentConfigData> dataMap;

	public DepartmentConfig() {
	    loadJson();
	}

    @Override
    public void loadJson() {
        this.dataList = ConfigUtil.loadDataList(DepartmentConfig.class, new TypeReference<List<DepartmentConfigData>>(){}.getType());
        this.dataMap = new HashMap<>();
        for (DepartmentConfigData configData : dataList) {
            dataMap.put(configData.getDepartmentId(), configData);
        }
	}
	
	public List<DepartmentConfigData> getDataList() {
		return dataList;
	}

    public Map<Integer, DepartmentConfigData> getDataMap() {
        return dataMap;
    }

    public DepartmentConfigData get(int departmentId) {
        return dataMap.get(departmentId);
	}
}
