package com.metahospital.datacollector.dao.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 2021/12/28.
 * todo 暂时(211228)直接配置在代码中，需要再定配置表处理办法，看看是存数据库，还是直接存文件，是否需要缓存在内存
 */
@Component
public class HospitalConfig {
	private List<HospitalConfigData> dataList;
	private Map<Integer, HospitalConfigData> dataMap;

	public HospitalConfig() {
		this.dataList = Stream.of(
				new HospitalConfigData(1, "这一家医院"),
				new HospitalConfigData(2, "那一家医院")
		).collect(Collectors.toList());
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
