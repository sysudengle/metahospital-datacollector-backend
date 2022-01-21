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
import java.util.stream.Collectors;

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
	    // Profile profile = profileDao.get(hospitalId, profileId);
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

        // 首次登陆写入数据库及缓存 TODO(max) 优化下述db操作合并为一个事务操作
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
        // TOREVIEW
        List<Hospital> hospitals =  scheduleService.getAllHospitals();
        List<HospitalDto> hospitalDtos = new ArrayList<>();

        for (Hospital hospital : hospitals) {
            hospitalDtos.add(new HospitalDto(hospital.getHospitalId(), hospital.getName()));
        }

        return hospitalDtos;
    }

    @Override
    public AddWXProfileRspDto upsertProfile(AddWXProfileReqDto addWXProfileReqDto) {
        AddWXProfileRspDto rspDto = new AddWXProfileRspDto();
        // TODO(max) 替换为全局统一id
        // TOREVIEW bookingId后台生成, 如果前端已知profileId，说明是更新操作，如果不是则新增
        long profileId = -1;
        if (addWXProfileReqDto.getProfileInfoDto().getProfileId() == -1) {
            profileId = new Random().nextLong();
        } else {
            profileId = addWXProfileReqDto.getProfileInfoDto().getProfileId();
        }
        // TOREVIEW profileId应该由后台生成
        userProfileDao.replace(new UserProfile(addWXProfileReqDto.getUserId(), addWXProfileReqDto.getProfileInfoDto().getHospitalId(),
                profileId));

        // REVIEWED a) 减少重复代码，可引用 b) 后续连续db操作考虑用事务处理
        ProfileInfoDto profile = addWXProfileReqDto.getProfileInfoDto();
        profileDao.replace(new Profile(profile.getHospitalId(),
                profileId, profile.getPersonalID(),
                profile.getGender().getValue(), profile.getPidAddress(),
                profile.getHomeAddress()));

        return rspDto;
    }

    @Override
    public GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto) {
        GetWXProfilesRspDto rspDto = new GetWXProfilesRspDto();
        // TOREVIEW 只用一次db的查询即可，用联表操作
        List<Profile> profiles = profileDao.getAll(getWXProfilesReqDto.getUserId());
        List<ProfileInfoDto> profileInfoDtos = new ArrayList<>();
        // TOREVIEW 不用全参数构造函数，避免改变成员变量顺序时候出错，以及多次db读改一次db读
        for (Profile p : profiles) {
            ProfileInfoDto pDto = new ProfileInfoDto();
            pDto.setProfileId(p.getProfileId());
            pDto.setHomeAddress(p.getHomeAddress());
            pDto.setPersonalID(p.getPersonalID());
            pDto.setHospitalId(p.getHospitalId());
            pDto.setPidAddress(p.getPidAddress());
            pDto.setGender(Gender.convert(p.getGender()));

            profileInfoDtos.add(pDto);
        }

        rspDto.setProfiles(profileInfoDtos);
        return rspDto;
    }

    // TOREVIEW 同时包含新增和编辑的功能，用upsert表示，update/insert的意思
    @Override
    public AddWXBookingRspDto upsertBooking(AddWXBookingReqDto addWXBookingReqDto) {
        AddWXBookingRspDto rspDto = new AddWXBookingRspDto();

        long profileId = addWXBookingReqDto.getProfileId();
        int hospitalId = addWXBookingReqDto.getHospitalId();
        // TODO(max) 替换为全局统一id
        // TOREVIEW bookingId后台生成, 如果前端已知bookingId，说明是更新操作，如果不是则新增
        long bookingId = -1;
        if (addWXBookingReqDto.getBookingInfoDto().getBookingId() == -1) {
            bookingId = new Random().nextLong();
        } else {
            bookingId = addWXBookingReqDto.getBookingInfoDto().getBookingId();
        }

        Date dateTime = addWXBookingReqDto.getBookingInfoDto().getDateTime();
        List<ComboDto> comboDtos = addWXBookingReqDto.getBookingInfoDto().getComboDtos();
        //前端是id和name是确定都传吗？需不需要只穿id 套餐名严格用数据库索引
        // TOREVIEW 仅仅在中间加入分隔符
        String comboIds = "";
        for (int i = 0; i < comboDtos.size(); i++) {
            ComboDto comboDto = comboDtos.get(i);

            comboIds += comboDto.getComboId();
            if (i != comboDtos.size() - 1) {
                comboIds += "#";
            }
        }
        BookingStatus bookingStatus;
        if (addWXBookingReqDto.getBookingInfoDto().getBookingStatus() == BookingStatus.Unknown) {
            bookingStatus = BookingStatus.Processing;
        } else {
            bookingStatus = addWXBookingReqDto.getBookingInfoDto().getBookingStatus();
        }

        bookingDao.replace(new Booking(hospitalId, profileId, bookingId, dateTime, comboIds, bookingStatus.getValue()));

        return rspDto;
    }

    @Override
    public GetWXBookingsRspDto getBookings(GetWXBookingsReqDto getAppointmentRspDto) {
        GetWXBookingsRspDto rspDto = new GetWXBookingsRspDto();
        long profileId = getAppointmentRspDto.getProfileId();
        int hospitalId = getAppointmentRspDto.getHospitalId();
        List<Booking> bookings = bookingDao.getAll(hospitalId, profileId);
        List<BookingInfoDto> bookingInfoDtos = new ArrayList<>();

        for (Booking booking : bookings) {
            Set<Integer> comboIds = booking.deserializeComboIds();
            List<Combo> combos = scheduleService.getRelatedCombos(comboIds);
            List<ComboDto> comboDtos = combos.stream().map(c -> new ComboDto(c.getComboId(), c.getComboName())).collect(Collectors.toList());

            BookingInfoDto bookingInfoDto = new BookingInfoDto();
            bookingInfoDto.setBookingId(booking.getBookingId());
            bookingInfoDto.setBookingStatus(BookingStatus.convert(booking.getBookingStatus()));
            bookingInfoDto.setComboDtos(comboDtos);
            bookingInfoDto.setDateTime(booking.getDateTime());
            bookingInfoDtos.add(bookingInfoDto);
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
