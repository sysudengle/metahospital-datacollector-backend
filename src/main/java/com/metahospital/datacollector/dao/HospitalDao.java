package com.metahospital.datacollector.dao;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.dao.data.Hospital;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HospitalDao {
    public List<Hospital> get() {
        SqlSession sqlSession = MysqlDao.getSqlSession();
        List<Hospital> hospitals;
        try {
            hospitals = sqlSession.selectList("HospitalMapper.get");
            if (hospitals == null || hospitals.isEmpty()) {
                return null;
            }
        } catch (CollectorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, e.getLocalizedMessage());
        } finally {
            MysqlDao.closeSqlSession();
        }

        return hospitals;
    }
}
