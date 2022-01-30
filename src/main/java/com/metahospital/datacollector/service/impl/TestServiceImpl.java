/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Last modified: 2021-12-18 22:28
 * Author: allendeng
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.service.impl;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.common.enums.BookingStatus;
import com.metahospital.datacollector.common.enums.DoctorStatus;
import com.metahospital.datacollector.common.enums.Gender;
import com.metahospital.datacollector.common.enums.UserType;
import com.metahospital.datacollector.dao.BookingDao;
import com.metahospital.datacollector.dao.InnerAccountDao;
import com.metahospital.datacollector.dao.ProfileDao;
import com.metahospital.datacollector.dao.RedisDao;
import com.metahospital.datacollector.dao.TestDao;
import com.metahospital.datacollector.dao.UserDao;
import com.metahospital.datacollector.dao.UserDoctorDao;
import com.metahospital.datacollector.dao.UserProfileDao;
import com.metahospital.datacollector.dao.WechatAccountDao;
import com.metahospital.datacollector.config.configs.HospitalConfig;
import com.metahospital.datacollector.dao.data.Booking;
import com.metahospital.datacollector.dao.data.InnerAccount;
import com.metahospital.datacollector.dao.data.Profile;
import com.metahospital.datacollector.dao.data.User;
import com.metahospital.datacollector.dao.data.UserDoctor;
import com.metahospital.datacollector.dao.data.UserProfile;
import com.metahospital.datacollector.dao.data.WechatAccount;
import com.metahospital.datacollector.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TestServiceImpl implements TestService {
    public static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    private TestDao testDao;
    @Autowired
    private RedisDao redisDao;
	@Autowired
	private WechatAccountDao wechatAccountDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private InnerAccountDao innerAccountDao;
	@Autowired
	private UserDoctorDao userDoctorDao;
	@Autowired
	private UserProfileDao userProfileDao;
	@Autowired
	private ProfileDao profileDao;
	@Autowired
	private BookingDao bookingDao;
	
    public TestServiceImpl() {

    }

    @Override
    public String testMergeData(String id, String name) {
        boolean hasSet = redisDao.set(id, name);
        LOGGER.info("Redis write:" + hasSet);
        String retVal = redisDao.get(id);
        LOGGER.info("Redis get:" + retVal);

        String hashMapId = id + "666";
        hasSet = redisDao.hSet(hashMapId, name, hashMapId + "|" + name);
        LOGGER.info("Redis hash write:" + hasSet);
        Map<String, String> retMap = redisDao.hGetAll(hashMapId);
        LOGGER.info("Redis hash read:" + retMap.toString());

        // REVIEWED 异常处理
        if (id == null || id.isEmpty()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR);
        }
        long userId = randomNextLong();
	    userDao.replace(new User(userId, "handsome_"+userId, UserType.Patient.getValue()));
	    User user = userDao.get(userId);
        wechatAccountDao.replace(new WechatAccount("aaa", "bbb", "ccc", user.getUserId()));
        WechatAccount wechatAccount = wechatAccountDao.get("aaa");
	    innerAccountDao.replace(new InnerAccount("aaa", "bbb", user.getUserId()));
	    InnerAccount innerAccount = innerAccountDao.get("aaa");
	    int hospitalId = HospitalConfig.get().getDataList().get(0).getHospitalId();
	    userDoctorDao.replace(new UserDoctor(userId, hospitalId, "handsome_"+userId, DoctorStatus.Unknown.getValue(), ""));
	    UserDoctor userDoctor = userDoctorDao.get(userId);
	    long profileId = randomNextLong();
	    userProfileDao.replace(new UserProfile(userId, hospitalId, profileId));
	    List<UserProfile> userProfile = userProfileDao.getAll(userId);
	    String personalID = "1111111000000";
	    profileDao.replace(new Profile(hospitalId, profileId, personalID, "", Gender.Male.getValue(), "abc", "abcd"));
//	    Profile profile = profileDao.get(hospitalId, profileId);
	    Profile profile1 = profileDao.getByPersonalID(hospitalId, personalID);
	    long bookingId = randomNextLong();
	    bookingDao.replace(new Booking(hospitalId, profileId, bookingId, new Date(), "", BookingStatus.Processing.getValue()));
	    List<Booking> booking = bookingDao.getAll(hospitalId, profileId);
        Booking booking1 = bookingDao.get(hospitalId, profileId, 11112);
        BookingStatus bookingStatus = BookingStatus.Completed;
        int a = bookingStatus.getValue();
        return id + "|" + name + "|" + wechatAccount.getUserId() + "|" + user.getName() + "|" + BookingStatus.valueOf(a) + a;
    }

	private long randomNextLong() {
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}
}
