package com.metahospital.datacollector.dao.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 2021/12/28.
 */
@Component
public class ComboConfig {
	private List<ComboConfigData> dataList;
	private Map<Integer, Map<Integer, ComboConfigData>> dataMap;

	public ComboConfig() {
		this.dataList = Stream.of(
				new ComboConfigData(1, 1, "A餐", Stream.of(1, 2).collect(Collectors.toList())),
				new ComboConfigData(1, 2, "B餐", Stream.of(1).collect(Collectors.toList())),
				new ComboConfigData(2, 1, "A餐", Stream.of(1).collect(Collectors.toList())),
				new ComboConfigData(2, 2, "B餐", Stream.of(2).collect(Collectors.toList()))
		).collect(Collectors.toList());
		this.dataMap = new HashMap<>();
		for (ComboConfigData configData : dataList) {
			Map<Integer, ComboConfigData> map = dataMap.computeIfAbsent(configData.getHospitalId(), id -> new HashMap<>());
			map.put(configData.getComboId(), configData);
		}
	}
	
	public List<ComboConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, Map<Integer, ComboConfigData>> getDataMap() {
		return dataMap;
	}

	public ComboConfigData get(int hospitalId, int comboId) {
		Map<Integer, ComboConfigData> map = dataMap.get(hospitalId);
		if (map == null) {
			return null;
		}
		return map.get(comboId);
	}
}
