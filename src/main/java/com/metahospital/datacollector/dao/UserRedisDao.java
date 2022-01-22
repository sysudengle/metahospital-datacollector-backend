package com.metahospital.datacollector.dao;

import com.metahospital.datacollector.dao.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author wanghaoyuan
 */
@Repository
@Component
public class UserRedisDao {
    private static final int ALIVE_SECOND = 60 * 60 * 24 * 7;
    @Autowired
    private RedisDao redisDao;
    
    public void setOpenId(User user, String openId) {
        redisDao.set(genOpenIdKey(openId), String.valueOf(user.getUserId()));
    }
    
    public boolean isExistOpenId(long userId, String openId) {
        return String.valueOf(userId).equals(redisDao.get(genOpenIdKey(openId)));
    }

    /**
     * 延长过期时间
     */
    public boolean expireOpenId(long userId, String openId) {
        return redisDao.expire(genOpenIdKey(openId), ALIVE_SECOND);
    }
    
    private String genOpenIdKey(String openId) {
        return genKey(PriKeyType.OPENID_TYPE, openId);
    }
    
    private String genKey(PriKeyType priKeyType, String id) {
        return String.format("%s_%s", priKeyType.getPrefix(), id);
    }

    public enum PriKeyType {
        OPENID_TYPE("0"),
        USERID_TYPE("1");

        private final String prefix;

        PriKeyType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }
    }
}
