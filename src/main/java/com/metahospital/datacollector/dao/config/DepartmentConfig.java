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
public class DepartmentConfig {
	private List<DepartmentConfigData> dataList;
	private Map<Integer, Map<Integer, DepartmentConfigData>> dataMap;

	public DepartmentConfig() {
		this.dataList = Stream.of(
				new DepartmentConfigData(1, 1, "外科", null),
				new DepartmentConfigData(1, 2, "口腔科", null),
				new DepartmentConfigData(2, 1, "外科", null),
				new DepartmentConfigData(2, 2, "口腔科", null)
		).collect(Collectors.toList());
		this.dataMap = new HashMap<>();
		for (DepartmentConfigData configData : dataList) {
			Map<Integer, DepartmentConfigData> map = dataMap.computeIfAbsent(configData.getHospitalId(), id -> new HashMap<>());
			map.put(configData.getDepartmentId(), configData);
		}
	}
	
	public List<DepartmentConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, Map<Integer, DepartmentConfigData>> getDataMap() {
		return dataMap;
	}

	public DepartmentConfigData get(int hospitalId, int departmentId) {
		Map<Integer, DepartmentConfigData> map = dataMap.get(hospitalId);
		if (map == null) {
			return null;
		}
		return map.get(departmentId);
	}
}
