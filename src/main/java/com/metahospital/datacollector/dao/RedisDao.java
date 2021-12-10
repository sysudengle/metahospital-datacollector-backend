/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.dao;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// redis数据访问类，线程安全接口可以直接使用
@Repository
@Component
public class RedisDao {
    public static final Logger LOGGER = LoggerFactory.getLogger(RedisDao.class);

    @Value("${collector.redis.data.alive-second}")
    private long aliveSecond;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    public boolean set(final String key, final String value) {
        boolean hasSet = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            hasSet = true;
        } catch (Exception ex) {
            // redis错误不应该影响主流程, 仅测试用
            throw new CollectorException(RestCode.REDIS_REQ_ERR, ex.getMessage());
        }

        return hasSet;
    }

    public String get(final String key) {
        String retVal = "";
        try {
            retVal = redisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            // redis错误不应该影响主流程, 仅测试用
            throw new CollectorException(RestCode.REDIS_REQ_ERR, ex.getMessage());
        }

        return retVal;
    }

    public boolean hSet(final String key, final String subKey, final String value) {
        boolean hasSet = false;
        try {
            redisTemplate.opsForHash().put(key, subKey, value);
            redisTemplate.expire(key, aliveSecond, TimeUnit.SECONDS);
            hasSet = true;
        } catch (Exception ex) {
            // redis错误不应该影响主流程
            // throw new CollectorException(RestCode.REDIS_REQ_ERR, ex.getMessage());
            LOGGER.error("hset err", ex.getLocalizedMessage());
        }

        return hasSet;
    }

    public Map<String, String> hGet(final String key) {
        try {
            Map<Object, Object> objMapRet = redisTemplate.opsForHash().entries(key);
            // 直接类型转换，确保写入的值就是string类型
            return (Map) objMapRet;
        } catch (Exception ex) {
            // redis错误不应该影响主流程
            LOGGER.error("hGet err", ex.getLocalizedMessage());
        }

        return Collections.emptyMap();
    }
}
