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
import com.metahospital.datacollector.dao.data.Booking;
import com.metahospital.datacollector.dao.data.Profile;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingDao {

    public List<Booking> getAll(int hospitalId, long profileId) {
        SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            map.put("hospitalId", hospitalId);
	        map.put("profileId", profileId);
            List<Booking> list = sqlSession.selectList("BookingMapper.getAll", map);
            if (list == null || list.isEmpty()) {
                return Collections.emptyList();
            }
            return list;
        } catch (CollectorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, e.getLocalizedMessage());
        } finally {
            MysqlDao.closeSqlSession();
        }
    }
    
	public void replace(Booking booking) {
	    SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            sqlSession.update("BookingMapper.replace", booking);
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
