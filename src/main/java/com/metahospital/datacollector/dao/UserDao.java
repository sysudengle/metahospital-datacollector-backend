/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 * -----
 * Last Modified: 2021-12-10 15:24:47
 * Modified By: haoyuan
 * -----
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 * -----
 */
package com.metahospital.datacollector.dao;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.dao.entity.User;
import com.metahospital.datacollector.dao.entity.WechatAccount;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO 接口未通
@Repository
public class UserDao {

    public User get(long userId) {
        SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            map.put("userId", userId);
            List<User> users = sqlSession.selectList("UserMapper.get", map);
            if (users == null || users.isEmpty()) {
                throw new CollectorException(RestCode.PARAM_INVALID_ERR);
            }
            return users.get(0);
        } catch (CollectorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, e.getLocalizedMessage());
        } finally {
            MysqlDao.closeSqlSession();
        }
    }
    
	public void replace(User user) {
	    SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            sqlSession.update("UserMapper.replace", user);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
            e.printStackTrace();
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, e.getLocalizedMessage());
        } finally {
            MysqlDao.closeSqlSession();
        }
    }
}
