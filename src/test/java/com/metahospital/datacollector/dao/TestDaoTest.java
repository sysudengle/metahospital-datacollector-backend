/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.dao;

import static org.junit.Assert.fail;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.dao.data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDaoTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(TestDaoTest.class);
    private TestDao testDao;

    @Before
    public void setup() {
        testDao = new TestDao();
    }

    @Test
    public void testGetUser() {
        User user = testDao.get("f2cae931c4fc6c65d0434a8d7365815f");
        Assert.assertNotNull(user);
    }

    @Test
    public void testCheckToken() {
        try {
            testDao.checkTokenValid("9cb0112d9fbdf9010b7e022270c4ca63");
        } catch (CollectorException ex) {
            LOGGER.error("check token error", ex);
            fail();
        }
    }
}
