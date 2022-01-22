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
 * todo 暂时(211228)直接配置在代码中，需要再定配置表处理办法，看看是存数据库，还是直接存文件，是否需要缓存在内存
 */
public class HospitalConfig implements IConfig {
    public static HospitalConfig get() {
        return ConfigService.getConfig(HospitalConfig.class);
    }

	private List<HospitalConfigData> dataList;
	private Map<Integer, HospitalConfigData> dataMap;

	public HospitalConfig() {
	    loadJson();
	}
	
    @Override
	public void loadJson() {
        this.dataList = ConfigUtil.loadDataList(HospitalConfig.class, new TypeReference<List<HospitalConfigData>>(){}.getType());
        this.dataMap = new HashMap<>();
        for (HospitalConfigData configData : dataList) {
            dataMap.put(configData.getHospitalId(), configData);
        }
	}
	
	public List<HospitalConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, HospitalConfigData> getDataMap() {
		return dataMap;
	}

	public HospitalConfigData get(int hospitalId) {
		return dataMap.get(hospitalId);
	}
}
