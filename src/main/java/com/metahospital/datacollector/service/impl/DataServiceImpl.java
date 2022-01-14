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
import com.metahospital.datacollector.common.enums.BookingStatus;
import com.metahospital.datacollector.common.enums.DoctorStatus;
import com.metahospital.datacollector.common.enums.Gender;
import com.metahospital.datacollector.common.enums.UserType;
import com.metahospital.datacollector.common.util.WechatUtil;
import com.metahospital.datacollector.controller.dto.*;
import com.metahospital.datacollector.dao.*;
import com.metahospital.datacollector.dao.config.*;
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
    @Autowired
    private  ComboConfig comboConfig;
    @Autowired
    private ScheduleServiceImpl scheduleService;
    @Autowired
    private HospitalDao hospitalDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private ComboDao comboDao;
    @Autowired
    private ComboItemsDao comboItemsDao;
	
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

        // REVIEWED 异常处理
        if (id == null || id.isEmpty()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR);
        }
        long userId = genUserId();
	    userDao.replace(new User(userId, "handsome_"+userId, UserType.Patient.getValue()));
	    User user = userDao.get(userId);
        wechatAccountDao.replace(new WechatAccount("aaa", "bbb", "ccc", user.getUserId()));
        WechatAccount wechatAccount = wechatAccountDao.get("aaa");
	    innerAccountDao.replace(new InnerAccount("aaa", "bbb", user.getUserId()));
	    InnerAccount innerAccount = innerAccountDao.get("aaa");
	    int hospitalId = hospitalConfig.getDataList().get(0).getHospitalId();
	    userDoctorDao.replace(new UserDoctor(userId, hospitalId, "handsome_"+userId, DoctorStatus.Unknown.getValue(), ""));
	    UserDoctor userDoctor = userDoctorDao.get(userId);
	    long profileId = genUserId();
	    userProfileDao.replace(new UserProfile(userId, hospitalId, profileId));
	    List<UserProfile> userProfile = userProfileDao.getAll(userId);
	    String personalID = "1111111000000";
	    profileDao.replace(new Profile(hospitalId, profileId, personalID, Gender.Male.getValue(), "abc", "abcd"));
	    Profile profile = profileDao.get(hospitalId, profileId);
	    Profile profile1 = profileDao.getByPersonalID(hospitalId, personalID);
	    long bookingId = genUserId();
	    bookingDao.replace(new Booking(hospitalId, profileId, bookingId, new Date(), "", BookingStatus.Processing.getValue()));
	    List<Booking> booking = bookingDao.getAll(hospitalId, profileId);
        Booking booking1 = bookingDao.getComboIds(11112);

        BookingStatus bookingStatus = BookingStatus.Completed;
        int a = bookingStatus.getValue();

        
        return id + "|" + name + "|" + wechatAccount.getUserId() + "|" + user.getName() + "|" + BookingStatus.convert(a) + a;
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
            setAuthRsp(rspDto, cacheKey, openId, userId, UserType.convert(user.getUserType()));
            LOGGER.info("auth hit db!");
            return rspDto;
        }

        // 首次登陆写入数据库及缓存 TODO 优化下述db操作合并为一个事务操作
        long newUserId = new Random().nextLong(); //暂时先用随机长型代替
        userDao.replace(new User(newUserId, "lee", UserType.Patient.getValue()));

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
        Hospital hospital = hospitalDao.get().get(hospitalId);
        String hospitalName = hospital.getName();
        //判断openId，userId是否配对存在于数据库
        long userId = registerWXDoctorReqDto.getUserId();

        UserDoctor userDoctor = userDoctorDao.get(userId);
        // REVIEWED 已存在注册信息,就不该使用该接口，通过异常码给到前端操作有误
        if (userDoctor != null) {
            throw new CollectorException(RestCode.DOCTOR_ALREADY_REGISTER_ERR);
        }

        userDoctorDao.replace(new UserDoctor(userId, hospitalId, staffId, DoctorStatus.UnderApply.getValue(), ""));

        rspDto.setOpenId(registerWXDoctorReqDto.getOpenId());
        rspDto.setUserId(userId);
        // TOREVIEW 减少if的嵌套，不引入圈复杂度
        return rspDto;
    }

    @Override
    public GetWXDoctorRspDto getDoctor(GetWXDoctorReqDto getWXDoctorReqDto) {
        // REVIEWED
        GetWXDoctorRspDto rspDto = new GetWXDoctorRspDto();
        long userId = getWXDoctorReqDto.getUserId();

        UserDoctor userDoctor = userDoctorDao.get(userId);
        if (userDoctor == null) {
            rspDto.setExists(false);

            return rspDto;
        }

        rspDto.setExists(true);
        rspDto.setDoctorStatus(DoctorStatus.convert(userDoctor.getStatus()));
        rspDto.setHospitalId(userDoctor.getHospitalId());
        Hospital hospital = scheduleService.getHospital(userDoctor.getHospitalId());
        rspDto.setHospitalName(hospital.getName());
        // TODO(alllen)

        return rspDto;
    }

    @Override
    public List<HospitalDto> getHospitals() {
        List<HospitalDto> hospitalDtos = new ArrayList<>();
        List<Hospital> hospitals = hospitalDao.get();
        for(int index = 0; index < hospitals.size(); index++)
        {
            int hospitalId = index;
            hospitalDtos.add(new HospitalDto(hospitals.get(hospitalId).getHospitalId(), hospitals.get(hospitalId).getName()));

        }
        return hospitalDtos;
    }

    @Override
    public AddWXProfileRspDto addProfile(AddWXProfileReqDto addWXProfileReqDto) {
        AddWXProfileRspDto rspDto = new AddWXProfileRspDto();
        userProfileDao.replace(new UserProfile(addWXProfileReqDto.getUserId(), addWXProfileReqDto.getProfileInfoDto().getHospitalId(), addWXProfileReqDto.getProfileInfoDto().getProfileId()));

        // REVIEWED a) 减少重复代码，可引用 b) 后续连续db操作考虑用事务处理
        ProfileInfoDto profile = addWXProfileReqDto.getProfileInfoDto();
        profileDao.replace(new Profile(profile.getHospitalId(),
                profile.getProfileId(), profile.getPersonalID(),
                profile.getGender().getValue(), profile.getPidAddress(),
                profile.getHomeAddress()));

        return rspDto;
    }

    @Override
    public GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto) {
        GetWXProfilesRspDto rspDto = new GetWXProfilesRspDto();
        List<UserProfile> userProfiles = userProfileDao.getAll(getWXProfilesReqDto.getUserId());

        //我需要以下列表类的内容作为返回值
        List<ProfileInfoDto> profileInfoDtos = new ArrayList<>();
        // REVIEWED 小范围下标直接用i即可
        for(int i = 0; i < userProfiles.size(); i++){
            long profileId = userProfiles.get(i).getProfileId();
            int hospitalId = userProfiles.get(i).getHospitalId();
            Profile profile = profileDao.get(hospitalId,profileId);
            profileInfoDtos.add(new ProfileInfoDto(profile.getProfileId(), profile.getHospitalId(),
                    profile.getPersonalID(), Gender.convert(profile.getGender()), profile.getPidAddress(),
                    profile.getHomeAddress()));
        }

        rspDto.setProfiles(profileInfoDtos);
        return rspDto;
    }

    @Override
    public AddWXBookingRspDto addBooking(AddWXBookingReqDto addWXBookingReqDto) {
        AddWXBookingRspDto rspDto = new AddWXBookingRspDto();

        long profileId = addWXBookingReqDto.getProfileId();
        int hospitalId = addWXBookingReqDto.getHospitalId();
        long bookingId = addWXBookingReqDto.getBookingInfoDto().getBookingId();
        Date dateTime = addWXBookingReqDto.getBookingInfoDto().getDateTime();
        List<ComboDto> comboDtos = addWXBookingReqDto.getBookingInfoDto().getComboDtos();


        //前端是id和name是确定都传吗？需不需要只穿id 套餐名严格用数据库索引
        String comboId = "#";
        String comboName = "#";

        for (int i = 0; i < comboDtos.size(); i++)
        {
            //感觉需要判空，和判误码，但我不知道该如何加
            comboId =  comboId + comboDtos.get(i).getComboId() + "#";
            comboName = comboName + comboDtos.get(i).getName() + "#";
        }

        bookingDao.replace(new Booking(hospitalId, profileId, bookingId, dateTime, comboId, BookingStatus.Processing.getValue()));
        rspDto.setHospitalId(hospitalId);
        rspDto.setProfileId(profileId);

        return rspDto;
    }

    @Override
    public GetWXBookingsRspDto getBookings(GetWXBookingsReqDto getAppointmentRspDto) {
        GetWXBookingsRspDto rspDto = new GetWXBookingsRspDto();
        long profileId = getAppointmentRspDto.getProfileId();
        int hospitalId = getAppointmentRspDto.getHospitalId();
        List<Booking> bookings = bookingDao.getAll(hospitalId, profileId);
        List<BookingInfoDto> bookingInfoDtos = new ArrayList<>();


        for(int i = 0; i < bookings.size(); i++){
            //分割字符串，提取comboid
            String[] sourceStrArray = bookings.get(i).getComboIds().split("#");
            List<ComboDto> comboDtos = new ArrayList<>();
            for(int j = 1; j < sourceStrArray.length; j++)
            {
                int comboId = Integer.parseInt(sourceStrArray[j]);
                Combo combo = comboDao.get(comboId);
                comboDtos.add(new ComboDto(comboId, combo.getComboName()));
            }
            long bookingId = bookings.get(i).getBookingId();
            Date dateTime = bookings.get(i).getDateTime();
            int bookingStatus = bookings.get(i).getBookingStatus();

            bookingInfoDtos.add(new BookingInfoDto(bookingId, dateTime, comboDtos, BookingStatus.values()[bookingStatus]));

        }

        rspDto.setBookings(bookingInfoDtos);

        return rspDto;
    }


    @Override
    public List<DepartmentDto> getDepartments(GetWXDepartmentsReqDto getWXDepartmentsReqDto){
        //openid 和 userid 要做判别和权限吗？

        List<DepartmentDto> departmentDtos = new ArrayList<>();
        List<Department> departments = departmentDao.get();
        for(int i = 0; i < departments.size(); i++)
        {
            departmentDtos.add(new DepartmentDto(departments.get(i).getDepartmentId(), departments.get(i).getName()));
        }

        return departmentDtos;

    }

    // 鉴权接口成功回包赋值
    private void setAuthRsp(AuthRspDto rspDtoRef, String cacheKey, String openId, long userId, UserType userType) {
        rspDtoRef.setOpenId(openId);
        rspDtoRef.setUserId(userId);
        rspDtoRef.setUType(userType);
        String userIdRedis = String.valueOf(userId);
        redisDao.set(cacheKey, userIdRedis);
    }


	private long genUserId() {
		// todo why 这个随便写的，有问题的，需要添加一个id生成工具
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}
}
