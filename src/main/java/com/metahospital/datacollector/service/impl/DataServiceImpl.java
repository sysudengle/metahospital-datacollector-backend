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

import com.alibaba.fastjson.JSONObject;
import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.common.enums.DoctorStatus;
import com.metahospital.datacollector.common.enums.UserType;
import com.metahospital.datacollector.common.util.WechatUtil;
import com.metahospital.datacollector.controller.dto.*;
import com.metahospital.datacollector.dao.*;
import com.metahospital.datacollector.dao.config.HospitalConfig;
import com.metahospital.datacollector.dao.data.InnerAccount;
import com.metahospital.datacollector.dao.data.User;
import com.metahospital.datacollector.dao.data.UserDoctor;
import com.metahospital.datacollector.dao.data.WechatAccount;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metahospital.datacollector.service.DataService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DataServiceImpl implements DataService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataServiceImpl.class);

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
	private HospitalConfig hospitalConfig;
	
    public DataServiceImpl() {

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

        // TOREVIEW 异常处理
        if (id == null || id.isEmpty()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR);
        }
        long userId = genUserId();
	    userDao.replace(new User(userId, "handsome_"+userId, UserType.Patient));
	    User user = userDao.get(userId);
        wechatAccountDao.replace(new WechatAccount("aaa", "bbb", "ccc", user.getUserId()));
        WechatAccount wechatAccount = wechatAccountDao.get("aaa");
	    innerAccountDao.replace(new InnerAccount("aaa", "bbb", user.getUserId()));
	    InnerAccount innerAccount = innerAccountDao.get("aaa");
	    userDoctorDao.replace(new UserDoctor(userId, hospitalConfig.getDataList().get(0).getHospitalId(), "handsome_"+userId, DoctorStatus.Unknown, ""));
	    UserDoctor userDoctor = userDoctorDao.get(userId);
        
        return id + "|" + name + "|" + wechatAccount.getUserId() + "|" + user.getName();
    }

    @Override
    public AuthRspDto authWX(AuthReqDto authReqDto) {
        AuthRspDto rspDto = new AuthRspDto();
        // 微信获取openId判断
        JSONObject wxJsonRsp = WechatUtil.getSessionKeyOrOpenId(authReqDto.getWechatJsCode());
        String openId = wxJsonRsp.getString("openid");
        if (StringUtils.isEmpty(openId)) {
            throw new CollectorException(RestCode.WECHAT_OPENID_INVALID_ERR);
        }
        // mock测试代码，TODEL(allen)
        // JSONObject wxJsonRsp = new JSONObject();
        // String openId = "666";

        // cache获取
        String cacheKey = RedisDao.PriKeyType.OPENID_TYPE.getPrefix() + openId;
        String userId = redisDao.get(cacheKey);
        if (!StringUtils.isEmpty(userId)) {
            // 回包前重新刷新openid，相当于session的能力
            setAuthRsp(rspDto, cacheKey, openId, userId);
            LOGGER.info("auth hit cache!");
            return rspDto;
        }

        // 缓存数据不存在则尝试从db获取
        WechatAccount wxAccount = wechatAccountDao.get(openId);
        if (wxAccount != null) {
            userId = String.valueOf(wxAccount.getUserId());
            setAuthRsp(rspDto, cacheKey, openId, userId);
            LOGGER.info("auth hit db!");
            return rspDto;
        }

        // 首次登陆写入数据库及缓存 TODO 优化下述db操作合并为一个事务操作
        long newUserId = new Random().nextLong(); //暂时先用随机长型代替
        userDao.replace(new User(newUserId, "", UserType.Patient));

        // mock测试代码，TODEL(allen)
        // User user = userDao.get(newUserId);
        // LOGGER.info("mock test db received: " + user.getUserId());

        // 注意要做null判空，不然写db会有问题
        String unionId = Optional.ofNullable(wxJsonRsp.getString("openid")).orElse("");
        String sessionKey = Optional.ofNullable(wxJsonRsp.getString("session_key")).orElse("");
        wechatAccountDao.replace(new WechatAccount(openId, unionId, sessionKey, newUserId));
        setAuthRsp(rspDto, cacheKey, openId, String.valueOf(newUserId));

        // mock测试代码，TODEL(allen)
        // String cacheVal = redisDao.get(cacheKey);
        // LOGGER.info("mock test redis received: " + cacheVal);

        return rspDto;
    }

    @Override
    public RegisterWXDoctorRspDto registerDoctor(RegisterWXDoctorReqDto registerWXDoctorReqDto) {
        RegisterWXDoctorRspDto rspDto = new RegisterWXDoctorRspDto();

        return rspDto;
    }

    @Override
    public List<HospitalDto> getHospitals() {
        List<HospitalDto> hospitalDtos = new ArrayList<>();

        return hospitalDtos;
    }

    @Override
    public AddWXProfileRspDto addProfile(AddWXProfileReqDto addWXProfileReqDto) {
        AddWXProfileRspDto rspDto = new AddWXProfileRspDto();

        return rspDto;
    }

    @Override
    public GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto) {
        GetWXProfilesRspDto rspDto = new GetWXProfilesRspDto();

        return rspDto;
    }

    @Override
    public AddWXBookingRspDto addBooking(AddWXBookingReqDto addWXBookingReqDto) {
        AddWXBookingRspDto rspDto = new AddWXBookingRspDto();

        return rspDto;
    }

    @Override
    public GetWXBookingsRspDto getBookings(GetWXBookingsReqDto getAppointmentRspDto) {
        GetWXBookingsRspDto rspDto = new GetWXBookingsRspDto();

        return rspDto;
    }

    // 鉴权接口成功回包赋值
    private void setAuthRsp(AuthRspDto rspDtoRef, String cacheKey, String openId, String userId) {
        rspDtoRef.setOpenId(openId);
        rspDtoRef.setUserId(userId);

        redisDao.set(cacheKey, userId);
    }

	private long genUserId() {
		// todo why 这个随便写的，有问题的，需要添加一个id生成工具
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}
}
