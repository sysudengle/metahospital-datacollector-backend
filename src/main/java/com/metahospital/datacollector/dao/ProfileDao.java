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
import com.metahospital.datacollector.dao.data.Profile;
import com.metahospital.datacollector.dao.data.UserProfile;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProfileDao {

    public List<Profile> getAll(long userId) {
        SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            // map.put("hospitalId", hospitalId);
	        map.put("userId", userId);
            List<Profile> profiles = sqlSession.selectList("ProfileMapper.get", map);
            if (profiles == null || profiles.isEmpty()) {
                return Collections.emptyList();
            }
            return profiles;
        } catch (CollectorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, e.getLocalizedMessage());
        } finally {
            MysqlDao.closeSqlSession();
        }
    }

	public Profile getByPersonalID(int hospitalId, String personalID) {
		SqlSession sqlSession = MysqlDao.getSqlSession();
		try {
			Map<String, Object> map = new HashMap();
			map.put("hospitalId", hospitalId);
			map.put("profileId", personalID);
			List<Profile> list = sqlSession.selectList("ProfileMapper.getByPersonalID", map);
			if (list == null || list.isEmpty()) {
				return null;
			}
			return list.get(0);
		} catch (CollectorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CollectorException(RestCode.PARAM_INVALID_ERR, e.getLocalizedMessage());
		} finally {
			MysqlDao.closeSqlSession();
		}
	}
    
	public void replace(Profile profile) {
	    SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            sqlSession.update("ProfileMapper.replace", profile);
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
