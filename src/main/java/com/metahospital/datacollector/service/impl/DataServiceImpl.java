/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.service.impl;

import com.metahospital.datacollector.aop.handler.ClientException;
import com.metahospital.datacollector.dao.TestDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metahospital.datacollector.service.DataService;

@Service
public class DataServiceImpl implements DataService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private TestDao testDao;

    public DataServiceImpl() {

    }

    @Override
    public String TestMergeData(String id, String name) {
        // TOREVIEW 异常处理
        if (id == null || id.isEmpty()) {
            throw new ClientException(ClientException.INVALID_PARAM);
        }

        return id + "|" + name;
    }
}
