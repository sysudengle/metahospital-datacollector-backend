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
import com.metahospital.datacollector.dao.RedisDao;
import com.metahospital.datacollector.dao.TestDao;
import com.metahospital.datacollector.dao.UserDao;
import com.metahospital.datacollector.dao.WechatAccountDao;
import com.metahospital.datacollector.dao.entity.User;
import com.metahospital.datacollector.dao.entity.WechatAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.core.IdentifierGenerator;
import org.springframework.stereotype.Service;

import com.metahospital.datacollector.service.DataService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataServiceImpl implements DataService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private TestDao testDao;
    @Autowired
    private RedisDao redisDao;
	@Autowired
	private WechatAccountDao wechatAccountDao;
	@Autowired
	private UserDao userDao;
	
    public DataServiceImpl() {

    }

    @Override
    public String TestMergeData(String id, String name) {
        boolean hasSet = redisDao.set(id, name);
        LOGGER.info("Redis write:" + hasSet);
        String retVal = redisDao.get(id);
        LOGGER.info("Redis get:" + retVal);

        String hashMapId = id + "666";
        hasSet = redisDao.hSet(hashMapId, name, hashMapId + "|" + name);
        LOGGER.info("Redis hash write:" + hasSet);
        Map<String, String> retMap = redisDao.hGet(hashMapId);
        LOGGER.info("Redis hash read:" + retMap.toString());

        // TOREVIEW 异常处理
        if (id == null || id.isEmpty()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR);
        }
        long userId = genUserId();
	    userDao.replace(new User(userId, "handsome_"+userId));
	    User user = userDao.get(userId);
        wechatAccountDao.replace(new WechatAccount("aaa", "bbb", "ccc", user.getUserId()));
        WechatAccount wechatAccount = wechatAccountDao.get("aaa");
        return id + "|" + name + "|" + wechatAccount.getUserId() + "|" + user.getName();
    }

	private long genUserId() {
		// todo why 这个随便写的，有问题的，需要添加一个id生成工具
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}
}
