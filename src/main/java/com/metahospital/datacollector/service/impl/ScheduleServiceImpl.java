/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleServiceImpl {
    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Scheduled(fixedRate = 60000)
    public void expireOldBosLock() {
    }
}
