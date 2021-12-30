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
public class ExaminationItemConfig {
	private List<ExaminationItemConfigData> dataList;
	private Map<Integer, ExaminationItemConfigData> dataMap;

	public ExaminationItemConfig() {
		this.dataList = Stream.of(
				new ExaminationItemConfigData(1, "视力"),
				new ExaminationItemConfigData(2, "身高")
		).collect(Collectors.toList());
		this.dataMap = new HashMap<>();
		for (ExaminationItemConfigData configData : dataList) {
			dataMap.put(configData.getExaminationItemId(), configData);
		}
	}
	
	public List<ExaminationItemConfigData> getDataList() {
		return dataList;
	}

	public Map<Integer, ExaminationItemConfigData> getDataMap() {
		return dataMap;
	}

	public ExaminationItemConfigData get(int examinationItemId) {
		return dataMap.get(examinationItemId);
	}
}
