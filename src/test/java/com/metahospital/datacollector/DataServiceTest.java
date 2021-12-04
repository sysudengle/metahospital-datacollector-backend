/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector;

import static org.junit.Assert.fail;

import com.metahospital.datacollector.service.impl.DataServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataServiceTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataServiceTest.class);

    DataServiceImpl dataService;

    @Before
    public void setup() {
        dataService = new DataServiceImpl();
    }

    @Test
    public void testCheck() {
        try {
            dataService.TestMergeData("123", "alllendeng");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("check unit test error", ex);
            fail();
        }
    }
}
