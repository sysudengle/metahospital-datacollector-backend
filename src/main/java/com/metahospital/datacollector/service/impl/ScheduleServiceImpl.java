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
import com.metahospital.datacollector.dao.configbak.ComboDao;
import com.metahospital.datacollector.dao.configbak.HospitalDao;
import com.metahospital.datacollector.dao.configbak.data.Combo;
import com.metahospital.datacollector.dao.configbak.data.Hospital;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class ScheduleServiceImpl {
    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private ComboDao comboDao;

    // 保留2份数据防止重复计算
    private Map<Integer, Hospital> mHospitalMap = new HashMap<>();
    private List<Hospital> mHospitals = new ArrayList<>();

    private List<Combo> mCombos = new ArrayList<>();

    // TODO(alllen) 启动后再接受接口请求
    // 线程安全策略
    @Scheduled(fixedRate = 5000)
    public void loadDbConfig() {
        // TOREVIEW
        List<Hospital> hospitals = hospitalDao.get();
        Map<Integer, Hospital> hospitalMap = new HashMap<>();
        for (int i = 0; i < hospitals.size(); i++) {
            Hospital hospital = hospitals.get(i);
            hospitalMap.put(hospital.getHospitalId(), hospital);
        }
        mHospitalMap = hospitalMap;
        mHospitals = hospitals;
        //System.out.println("load hospitals with size: " + hospitalMap.size());

        List<Combo> combos = comboDao.getAll();
        mCombos = combos;
    }

    public Hospital getHospital(int hospitalId) {
        if (!mHospitalMap.containsKey(hospitalId)) {
            // 医院数据未获取到应中断流程
            throw new CollectorException(RestCode.HOSPITAL_INVALID_ERR);
        }

        return mHospitalMap.get(hospitalId);
    }

    public List<Hospital> getAllHospitals() {
        return mHospitals;
    }

    public List<Combo> getAllCombos() {
        return mCombos;
    }

    public List<Combo> getRelatedCombos(Set<Integer> comboIds) {
        List<Combo> combos = new ArrayList<>();
        for (Combo combo : mCombos) {
            if (comboIds.contains(combo.getComboId())) {
                combos.add(combo);
            }
        }

        return combos;
    }
}
