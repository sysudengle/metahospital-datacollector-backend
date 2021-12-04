/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.metahospital.datacollector.dao.mysql.MybatisUtil;
import com.metahospital.datacollector.dao.mysql.entity.User;
import org.apache.ibatis.session.SqlSession;

import com.metahospital.datacollector.aop.handler.ClientException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// TODO 接口未通
@Repository
@Component
public class TestDao {

    public User get(String userKey) {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            map.put("userKey", userKey);
            List<User> users = sqlSession.selectList("UserID.get", map);
            if (users == null || users.isEmpty()) {
                throw new ClientException(ClientException.INVALID_PARAM);
            }

            return users.get(0);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClientException(e.getLocalizedMessage());
        } finally {
            MybatisUtil.closeSqlSession();
        }
    }

    public void checkTokenValid(String token) throws ClientException {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            map.put("token", token);
            List<String> tokens = sqlSession.selectList("UserID.getUserToken", map);
            if (tokens == null || tokens.isEmpty()) {
                throw new ClientException(ClientException.INVALID_PARAM);
            }

        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClientException(e.getLocalizedMessage());
        } finally {
            MybatisUtil.closeSqlSession();
        }
    }

    public String getObjectKey(String userId, String dataId) {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            if (userId != null) {
                map.put("userId", userId);
            }

            map.put("dataId", dataId);
            List<String> objectKeys = sqlSession.selectList("UserID.getObjectKey", map);
            if (objectKeys == null || objectKeys.isEmpty()) {
                throw new ClientException(ClientException.INVALID_PARAM);
            }

            return objectKeys.get(0);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClientException(e.getLocalizedMessage());
        } finally {
            MybatisUtil.closeSqlSession();
        }
    }

    public String getObjectKey(String dataId) {
        return getObjectKey(null, dataId);
    }

    public void updateDataTime(String dataId) {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            map.put("dataId", dataId);
            map.put("updateTime", new Date());
            sqlSession.update("UserID.updateDataTime", map);
            sqlSession.commit();

        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
            throw new ClientException(e.getLocalizedMessage());
        } finally {
            MybatisUtil.closeSqlSession();
        }
    }

    public static void main(String[] args) {
        TestDao testDao = new TestDao();
        User user = testDao.get("f2cae931c4fc6c65d0434a8d7365815f");
        System.out.println(user);

        String objKey = testDao.getObjectKey("1", "3");
        System.out.println(objKey);

        testDao.updateDataTime("3");
    }
}
