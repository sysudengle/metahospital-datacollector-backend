/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.service.impl;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.dao.HospitalDao;
import com.metahospital.datacollector.dao.data.Hospital;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class ScheduleServiceImpl {
    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private HospitalDao hospitalDao;

    private Map<Integer, Hospital> hospitalMap = new HashMap<>();

    @Scheduled(fixedRate = 5000)
    public void loadDbConfig() {
        // TOREVIEW
        List<Hospital> hospitals = hospitalDao.get();
        for (int i = 0; i < hospitals.size(); i++) {
            Hospital hospital = hospitals.get(i);
            hospitalMap.put(hospital.getHospitalId(), hospital);
        }

        //System.out.println("load hospitals with size: " + hospitalMap.size());
    }

    public Hospital getHospital(int hospitalId) {
        if (!hospitalMap.containsKey(hospitalId)) {
            // 医院数据未获取到应中断流程
            throw new CollectorException(RestCode.HOSPITAL_INVALID_ERR);
        }

        return hospitalMap.get(hospitalId);
    }
}
