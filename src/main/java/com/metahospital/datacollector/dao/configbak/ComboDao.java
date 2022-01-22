package com.metahospital.datacollector.dao.configbak;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.dao.MysqlDao;
import com.metahospital.datacollector.dao.configbak.data.Combo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ComboDao {
    public List<Combo> getAll() {
        SqlSession sqlSession = MysqlDao.getSqlSession();
        List<Combo> combos;
        try {
            combos = sqlSession.selectList("ComboMapper.getAll");
            if (combos == null || combos.isEmpty()) {
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

        return combos;
    }

    public Combo get(int comboId){
        SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            map.put("comboId", comboId);
            List<Combo> list = sqlSession.selectList("ComboMapper.get", map);
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
}
