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
import com.metahospital.datacollector.common.enums.Gender;
import com.metahospital.datacollector.common.enums.UserType;
import com.metahospital.datacollector.common.util.WechatUtil;
import com.metahospital.datacollector.controller.dto.*;
import com.metahospital.datacollector.dao.*;
import com.metahospital.datacollector.dao.config.DepartmentConfig;
import com.metahospital.datacollector.dao.config.DepartmentConfigData;
import com.metahospital.datacollector.dao.config.HospitalConfig;
import com.metahospital.datacollector.dao.config.HospitalConfigData;
import com.metahospital.datacollector.dao.data.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metahospital.datacollector.service.DataService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
	private UserProfileDao userProfileDao;
	@Autowired
	private ProfileDao profileDao;
	@Autowired
	private BookingDao bookingDao;
	@Autowired
	private HospitalConfig hospitalConfig;
    @Autowired
    private DepartmentConfig departmentConfig;
	
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
	    int hospitalId = hospitalConfig.getDataList().get(0).getHospitalId();
	    userDoctorDao.replace(new UserDoctor(userId, hospitalId, "handsome_"+userId, DoctorStatus.Unknown, ""));
	    UserDoctor userDoctor = userDoctorDao.get(userId);
	    long profileId = genUserId();
	    userProfileDao.replace(new UserProfile(userId, hospitalId, profileId));
	    List<UserProfile> userProfile = userProfileDao.getAll(userId);
	    String personalID = "1111111000000";
	    profileDao.replace(new Profile(hospitalId, profileId, personalID, Gender.Male, "abc", "abcd"));
	    Profile profile = profileDao.get(hospitalId, profileId);
	    Profile profile1 = profileDao.getByPersonalID(hospitalId, personalID);
	    long bookingId = genUserId();
	    bookingDao.replace(new Booking(hospitalId, profileId, bookingId, new Date(), ""));
	    List<Booking> booking = bookingDao.getAll(hospitalId, profileId);
        
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

        // cache获取redis存的userid
        String cacheKey = RedisDao.PriKeyType.OPENID_TYPE.getPrefix() + openId;
        //String userId = redisDao.get(cacheKey);
        //long userId = genUserId();
        //!StringUtils.isEmpty(userId)

        /*if (userId != 0) {
            // 回包前重新刷新openid，相当于session的能力
            setAuthRsp(rspDto, cacheKey, openId, userId, UserType.Patient);
            LOGGER.info("auth hit cache!");
            return rspDto;
        }*/

        //缓存数据不存在则尝试从db获取
        WechatAccount wxAccount = wechatAccountDao.get(openId);
        if (wxAccount != null) {
            //userId = String.valueOf(wxAccount.getUserId());
            //从存在的openid索引userid 再从存在的userid索引usertype
            //这样成功注册的医生就可以微信登录即为医生。
            long userId = wxAccount.getUserId();
            User user = userDao.get(userId);
            setAuthRsp(rspDto, cacheKey, openId, userId, user.getUserType());
            LOGGER.info("auth hit db!");
            return rspDto;
        }

        // 首次登陆写入数据库及缓存 TODO 优化下述db操作合并为一个事务操作
        long newUserId = new Random().nextLong(); //暂时先用随机长型代替
        userDao.replace(new User(newUserId, "lee", UserType.Patient));

        // mock测试代码，TODEL(allen)
        // User user = userDao.get(newUserId);
        // LOGGER.info("mock test db received: " + user.getUserId());

        // 注意要做null判空，不然写db会有问题
        String unionId = Optional.ofNullable(wxJsonRsp.getString("unionid")).orElse("");
        String sessionKey = Optional.ofNullable(wxJsonRsp.getString("session_key")).orElse("");
        //db录入
        wechatAccountDao.replace(new WechatAccount(openId, unionId, sessionKey, newUserId));
        //redis录入，前端输出
        setAuthRsp(rspDto, cacheKey, openId, newUserId, UserType.Patient);

        // mock测试代码，TODEL(allen)
        // String cacheVal = redisDao.get(cacheKey);
        // LOGGER.info("mock test redis received: " + cacheVal);

        return rspDto;
    }

    @Override
    public RegisterWXDoctorRspDto registerDoctor(RegisterWXDoctorReqDto registerWXDoctorReqDto) {
        RegisterWXDoctorRspDto rspDto = new RegisterWXDoctorRspDto();
        String staffId = registerWXDoctorReqDto.getStaffId();
        //医院id和医院名字
        int hospitalId = registerWXDoctorReqDto.getHospitalId();
        HospitalConfigData hospitalConfigData = hospitalConfig.get(hospitalId);
        String hospitalName = hospitalConfigData.getHospitalName();
        //判断openId，userId是否配对存在于数据库
        String openId = registerWXDoctorReqDto.getOpenId();
        long userId = registerWXDoctorReqDto.getUserId();

        UserDoctor userDoctor = userDoctorDao.get(userId);
        //已存在注册信息
        if(userDoctor != null){
            //假设web端通过申请，注册成功并返回生成账户等信息。
            if(userDoctor.getStatus() == DoctorStatus.Valid){
                //虚构用户名和密码
                InnerAccount innerAccount = new InnerAccount("accName", "password",userId);
                innerAccountDao.replace(innerAccount);
                //用update更新db里的usertype,数据库操作还再更新，稍微用复杂一些的方法。
                User user = userDao.get(userId);
                String name = user.getName();
                userDao.replace(new User(userId, name, UserType.Doctor));
                //假设web端给定科室1
                int departmentId = 1;
                DepartmentConfigData departmentConfigData = departmentConfig.get(hospitalId,departmentId);
                String departmentName = departmentConfigData.getDepartmentName();

                setRegisterWXDoctorRsp(rspDto, userDoctor.getStatus(), UserType.Doctor, innerAccount.getAccountName(),
                        innerAccount.getPassword(), hospitalName, departmentName);
            }

            // 注册失败、无效和代办,均返回审核状态。
            else{
                setRegisterWXDoctorRsp(rspDto, userDoctor.getStatus(), UserType.Patient,
                        null, null, hospitalName, null);
            }
            return rspDto;
        }

        //不存在注册信息
        else {
            //这里不应该用replace的逻辑，但是前面已经为已存在注册信息限定了条件,问题不大。
            //医生注册信息录入数据库，并返回待定状态。
            userDoctorDao.replace(new UserDoctor(userId, hospitalId, staffId, DoctorStatus.UnderApply, ""));
            setRegisterWXDoctorRsp(rspDto, DoctorStatus.UnderApply, UserType.Patient,
                    null, null, hospitalName, null);
        }

        return rspDto;
    }

    @Override
    public List<HospitalDto> getHospitals() {
        List<HospitalDto> hospitalDtos = new ArrayList<>();
        for(int index = 1; index < 3; index++)
        {
            int hospitalId = index;
            HospitalConfigData hospitalConfigData = hospitalConfig.get(hospitalId);
            hospitalDtos.add(new HospitalDto(hospitalConfigData.getHospitalId(), hospitalConfigData.getHospitalName()));

        }
        return hospitalDtos;
    }

    @Override
    public AddWXProfileRspDto addProfile(AddWXProfileReqDto addWXProfileReqDto) {
        AddWXProfileRspDto rspDto = new AddWXProfileRspDto();
        userProfileDao.replace(new UserProfile(addWXProfileReqDto.getUserId(), addWXProfileReqDto.getProfileInfoDto().getHospitalId(), addWXProfileReqDto.getProfileInfoDto().getProfileId()));

        profileDao.replace(new Profile(addWXProfileReqDto.getProfileInfoDto().getHospitalId(),
                addWXProfileReqDto.getProfileInfoDto().getProfileId(), addWXProfileReqDto.getProfileInfoDto().getPersonalID(),
                addWXProfileReqDto.getProfileInfoDto().getGender(), addWXProfileReqDto.getProfileInfoDto().getPidAddress(),
                addWXProfileReqDto.getProfileInfoDto().getHomeAddress()));

        return rspDto;
    }

    @Override
    public GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto) {
        GetWXProfilesRspDto rspDto = new GetWXProfilesRspDto();
        List<UserProfile> userProfiles = userProfileDao.getAll(getWXProfilesReqDto.getUserId());

        //我需要以下列表类的内容作为返回值
        List<ProfileInfoDto> profileInfoDtos = new ArrayList<>();

        for(int index = 1; index < userProfiles.size() + 1; index++){
            long profileId = userProfiles.get(index).getProfileId();
            int hospitalId = userProfiles.get(index).getHospitalId();
            Profile profile = profileDao.get(hospitalId,profileId);
            profileInfoDtos.add(new ProfileInfoDto(profile.getProfileId(), profile.getHospitalId(),
                    profile.getPersonalID(), profile.getGender(), profile.getPidAddress(),
                    profile.getHomeAddress()));
        }


        rspDto.setProfiles(profileInfoDtos);
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
    private void setAuthRsp(AuthRspDto rspDtoRef, String cacheKey, String openId, long userId, UserType userType) {
        rspDtoRef.setOpenId(openId);
        rspDtoRef.setUserId(userId);
        rspDtoRef.setUType(userType);
        String userIdRedis = String.valueOf(userId);
        redisDao.set(cacheKey, userIdRedis);
    }

    //注册成功回包赋值
    private void setRegisterWXDoctorRsp(RegisterWXDoctorRspDto rspDtoRef, DoctorStatus doctorStatus,
                                        UserType userType, String accountName, String password,
                                        String hospitalName, String departmentName){
        rspDtoRef.setDoctorStatus(doctorStatus);
        rspDtoRef.setUType(userType);
        rspDtoRef.setAccountName(accountName);
        rspDtoRef.setPassword(password);
        rspDtoRef.setHospitalName(hospitalName);
        rspDtoRef.setDepartmentName(departmentName);
    }

	private long genUserId() {
		// todo why 这个随便写的，有问题的，需要添加一个id生成工具
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}
}
