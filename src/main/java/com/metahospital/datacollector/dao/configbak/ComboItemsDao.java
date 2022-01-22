package com.metahospital.datacollector.dao.configbak;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.dao.MysqlDao;
import com.metahospital.datacollector.dao.configbak.data.ComboItems;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ComboItemsDao {
    public List<ComboItems> getAll() {
        SqlSession sqlSession = MysqlDao.getSqlSession();
        List<ComboItems> comboItems;
        try {
            comboItems = sqlSession.selectList("ComboItemsMapper.getAll");
            if (comboItems == null || comboItems.isEmpty()) {
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

        return comboItems;
    }

    public ComboItems get(int comboId){
        SqlSession sqlSession = MysqlDao.getSqlSession();
        try {
            Map<String, Object> map = new HashMap();
            map.put("comboId", comboId);
            List<ComboItems> list = sqlSession.selectList("ComboItemMapper.get", map);
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
