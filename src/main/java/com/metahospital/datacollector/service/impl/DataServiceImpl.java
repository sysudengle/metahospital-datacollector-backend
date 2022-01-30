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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Sets;
import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.common.RestResponse;
import com.metahospital.datacollector.common.enums.BookingStatus;
import com.metahospital.datacollector.common.enums.DoctorStatus;
import com.metahospital.datacollector.common.enums.Gender;
import com.metahospital.datacollector.common.enums.UserType;
import com.metahospital.datacollector.common.util.StringUtil;
import com.metahospital.datacollector.common.util.JsonUtil;
import com.metahospital.datacollector.common.util.TimeUtil;
import com.metahospital.datacollector.common.util.WechatUtil;
import com.metahospital.datacollector.config.DataConst;
import com.metahospital.datacollector.controller.dto.*;
import com.metahospital.datacollector.dao.*;
import com.metahospital.datacollector.config.configs.*;
import com.metahospital.datacollector.dao.data.*;
import com.metahospital.datacollector.service.DataService;
import com.metahospital.datacollector.service.impl.itemhandler.IItemHandler;
import com.metahospital.datacollector.service.impl.itemhandler.ItemHandlerManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private UserRedisDao userRedisDao;
    @Autowired
    private WechatAccountDao wechatAccountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDoctorDao userDoctorDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private DepartmentItemsDao departmentItemsDao;
    @Autowired
    private ItemHandlerManager itemHandlerManager;

    public DataServiceImpl() {
    }

    @Override
    public AuthRspDto authWX(AuthReqDto authReqDto) {
        // 微信获取openId判断
        JSONObject wxJsonRsp = WechatUtil.getSessionKeyOrOpenId(authReqDto.getWechatJsCode());
        String openId = wxJsonRsp.getString("openid");
        if (StringUtils.isEmpty(openId)) {
            throw new CollectorException(RestCode.WECHAT_OPENID_INVALID_ERR, "获取微信openId失败");
        }
        WechatAccount wxAccount = wechatAccountDao.get(openId);
        if (wxAccount != null) {
            LOGGER.info("auth hit db!");
            // 从存在的openid索引userid 再从存在的userid索引usertype，这样成功注册的医生就可以微信登录即为医生。
            User user = userDao.get(wxAccount.getUserId());
            userRedisDao.setOpenId(user, openId);
            return genAuthRsp(user, loadValidDoctorInfoDto(user), openId);
        }
        // 首次登陆写入数据库及缓存 
        // TODO(max) 优化下述db操作合并为一个事务操作
        long newUserId = genUserId();
        // 注意要做null判空，不然写db会有问题
        String unionId = Optional.ofNullable(wxJsonRsp.getString("unionid")).orElse("");
        String sessionKey = Optional.ofNullable(wxJsonRsp.getString("session_key")).orElse("");
        // db录入
        User newUser = new User(newUserId, "lee", UserType.Patient.getValue());
        userDao.replace(newUser);
        wechatAccountDao.replace(new WechatAccount(openId, unionId, sessionKey, newUserId));
        // redis录入，前端输出
        userRedisDao.setOpenId(newUser, openId);
        return genAuthRsp(newUser, loadValidDoctorInfoDto(newUser), openId);
    }

    private long genUserId() {
        // todo why == 这个随便写的，有问题的，需要添加一个id生成工具
        return System.nanoTime();
    }

    /**
     * 加载已授权的医生信息，如果缺少信息或未授权，那么返回null
     */
    private DoctorInfoDto loadValidDoctorInfoDto(User user) {
        UserDoctor doctor = userDoctorDao.get(user.getUserId());
        if (doctor == null) {
            return null;
        }
        if (doctor.getStatus() != DoctorStatus.Valid.getValue()) {
            return null;
        }
        HospitalConfigData configData = HospitalConfig.get().get(doctor.getHospitalId());
        return genDoctorInfoDto(doctor, configData);
    }

    private DoctorInfoDto genDoctorInfoDto(UserDoctor doctor, HospitalConfigData configData) {
        DoctorInfoDto doctorInfoDto = new DoctorInfoDto();
        doctorInfoDto.setHospital(genHospitalDto(configData.getHospitalId(), configData.getHospitalName()));
        doctorInfoDto.setStatus(DoctorStatus.valueOf(doctor.getStatus()));
        doctorInfoDto.setDepartmentIds(JsonUtil.loadIntegerList(doctor.getDepartmentIds()));
        return doctorInfoDto;
    }

    private HospitalDto genHospitalDto(int hospitalId, String hospitalName) {
        HospitalDto hospitalDto = new HospitalDto();
        hospitalDto.setHospitalId(hospitalId);
        hospitalDto.setName(hospitalName);
        return hospitalDto;
    }

    private AuthRspDto genAuthRsp(User user, @Nullable DoctorInfoDto doctorInfoDto, String openId) {
        AuthRspDto authRspDto = new AuthRspDto();
        authRspDto.setOpenId(openId);
        authRspDto.setUserId(user.getUserId());
        authRspDto.setUType(UserType.valueOf(user.getUserType()));
        authRspDto.setDoctorInfo(doctorInfoDto);
        return authRspDto;
    }

    @Override
    public RegisterWXDoctorRspDto registerDoctor(RegisterWXDoctorReqDto registerWXDoctorReqDto) {
        int hospitalId = registerWXDoctorReqDto.getHospitalId();
        String staffId = registerWXDoctorReqDto.getStaffId();
        HospitalConfigData hospital = HospitalConfig.get().get(hospitalId);
        if (hospital == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的医院Id");
        }
        long userId = registerWXDoctorReqDto.getUserId();
        UserDoctor userDoctor = userDoctorDao.get(userId);
        // REVIEWED 已存在注册信息,就不该使用该接口，通过异常码给到前端操作有误
        if (userDoctor != null) {
            throw new CollectorException(RestCode.DOCTOR_ALREADY_REGISTER_ERR, "已存在医生信息");
        }
        userDoctorDao.replace(new UserDoctor(userId, hospitalId, staffId, DoctorStatus.UnderApply.getValue(), ""));
        return new RegisterWXDoctorRspDto();
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
        rspDto.setDoctorStatus(DoctorStatus.valueOf(userDoctor.getStatus()));
        rspDto.setHospitalId(userDoctor.getHospitalId());
        HospitalConfigData hospital = HospitalConfig.get().get(userDoctor.getHospitalId());
        rspDto.setHospitalName(hospital.getHospitalName());
        // TODO(alllen)
        return rspDto;
    }

    @Override
    public List<HospitalDto> getHospitals() {
        List<HospitalDto> hospitalDtos = new ArrayList<>();
        List<HospitalConfigData> hospitals = HospitalConfig.get().getDataList();
        for (HospitalConfigData hospital : hospitals) {
            hospitalDtos.add(new HospitalDto(hospital.getHospitalId(), hospital.getHospitalName()));
        }
        return hospitalDtos;
    }

    @Override
    public AddWXProfileRspDto upsertProfile(AddWXProfileReqDto addWXProfileReqDto) {
        long userId = addWXProfileReqDto.getUserId();
        ProfileInfoDto profileInfoDto = addWXProfileReqDto.getProfileInfoDto();
        HospitalConfigData configData = HospitalConfig.get().get(profileInfoDto.getHospitalId());
        if (configData == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的医院Id");
        }
        if (!checkPersonalID(profileInfoDto.getPersonalID())) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的身份证信息");
        }
        if (profileInfoDto.getGender() == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "性别不能为空");
        }
        StringUtil.checkStringNotEmptyWithLengthLimit(profileInfoDto.getFullName(), DataConst.FULL_NAME_LENGTH_LIMIT, "姓名");
        StringUtil.checkStringNotEmptyWithLengthLimit(profileInfoDto.getPidAddress(), DataConst.PID_ADDRESS_LENGTH_LIMIT, "身份证地址");
        StringUtil.checkStringNotEmptyWithLengthLimit(profileInfoDto.getHomeAddress(), DataConst.HOME_ADDRESS_LENGTH_LIMIT, "家庭住址");
        Profile oldProfile = profileDao.getByPersonalID(configData.getHospitalId(), profileInfoDto.getPersonalID());
        if (oldProfile != null && oldProfile.getProfileId() != profileInfoDto.getProfileId()) {
            // 可以修改，但同一身份证不能重复添加
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "该医院已存在该用户档案，不能重复添加");
        }
        List<UserProfile> userProfiles = userProfileDao.getAll(userId);
        long profileId = profileInfoDto.getProfileId();
        if (userProfiles.stream().noneMatch(userProfile -> userProfile.getProfileId() == profileInfoDto.getProfileId())) {
            profileId = genProfileId(userProfiles);
        }
        if (profileId != profileInfoDto.getProfileId() && userProfiles.size() >= DataConst.USER_PROFILE_NUMBER_LIMIT) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("用户绑定的档案数已达到上限%s", DataConst.USER_PROFILE_NUMBER_LIMIT));
        }
        UserProfile newUserProfile = new UserProfile(userId,
                profileInfoDto.getHospitalId(),
                profileId);
        Profile newProfile = new Profile(profileInfoDto.getHospitalId(),
                profileId,
                profileInfoDto.getPersonalID(),
                profileInfoDto.getFullName(),
                profileInfoDto.getGender().getValue(),
                profileInfoDto.getPidAddress(),
                profileInfoDto.getHomeAddress());
        // REVIEWED a) 减少重复代码，可引用 b) 后续连续db操作考虑用事务处理
        userProfileDao.replace(newUserProfile);
        profileDao.replace(newProfile);
        return new AddWXProfileRspDto(transProfileInfo(newProfile));
    }

    private boolean checkPersonalID(String personalID) {
        // todo why == 添加身份证验证
        if (StringUtils.isEmpty(personalID)) {
            return false;
        }
        return true;
    }

    private long genProfileId(List<UserProfile> userProfiles) {
        // TODO(max) 替换为全局统一id
        return userProfiles.stream().mapToLong(UserProfile::getProfileId).max().orElse(0) + 1;
    }

    @Override
    public GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto) {
        // todo why == 目前设想profile表是按医院进行分表，而userProfile表是按用户进行分表，在分表后，联表操作要对应修改下
        // TOREVIEW 只用一次db的查询即可，用联表操作
        List<Profile> profiles;
        if (HospitalConfig.get().get(getWXProfilesReqDto.getHospitalId()) != null) {
            profiles = profileDao.getAllByHospitalId(getWXProfilesReqDto.getUserId(), getWXProfilesReqDto.getHospitalId());
        } else {
            profiles = profileDao.getAllByUserId(getWXProfilesReqDto.getUserId());
        }
        GetWXProfilesRspDto getWXProfilesRspDto = new GetWXProfilesRspDto();
        getWXProfilesRspDto.setProfiles(profiles.stream().map(this::transProfileInfo).collect(Collectors.toList()));
        return getWXProfilesRspDto;
    }

    private ProfileInfoDto transProfileInfo(Profile profile) {
        // TOREVIEW 不用全参数构造函数，避免改变成员变量顺序时候出错，以及多次db读改一次db读
        ProfileInfoDto profileInfoDto = new ProfileInfoDto();
        profileInfoDto.setProfileId(profile.getProfileId());
        profileInfoDto.setHomeAddress(profile.getHomeAddress());
        profileInfoDto.setPersonalID(profile.getPersonalID());
        profileInfoDto.setHospitalId(profile.getHospitalId());
        profileInfoDto.setPidAddress(profile.getPidAddress());
        profileInfoDto.setGender(Gender.valueOf(profile.getGender()));
        profileInfoDto.setFullName(profile.getFullName());
        return profileInfoDto;
    }

    /**
     * TOREVIEW 同时包含新增和编辑的功能，用upsert表示，update/insert的意思
     */
    // 
    @Override
    public AddWXBookingRspDto upsertBooking(AddWXBookingReqDto addWXBookingReqDto) {
        long userId = addWXBookingReqDto.getUserId();
        long profileId = addWXBookingReqDto.getProfileId();
        int hospitalId = addWXBookingReqDto.getHospitalId();
        BookingInfoDto bookingInfoDto = addWXBookingReqDto.getBookingInfoDto();
        if (bookingInfoDto == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "缺少预约信息");
        }
        checkUserProfile(userId, hospitalId, profileId);
        Date dateTime = bookingInfoDto.getDateTime();
        if (dateTime == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "预约时间不能为空");
        }
        LocalDate bookingDateTime = TimeUtil.toDefaultLocalDate(dateTime);
        if (bookingDateTime.isBefore(LocalDate.now())) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的预约时间");
        }
        List<ComboDto> comboDtos = bookingInfoDto.getComboDtos();
        if (CollectionUtils.isEmpty(comboDtos)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "预约套餐不能为空");
        }
        Set<Integer> comboIds = comboDtos.stream().mapToInt(ComboDto::getComboId).boxed().collect(Collectors.toSet());
        ComboConfig comboConfig = ComboConfig.get();
        if (!comboIds.stream().allMatch(comboId -> comboConfig.get(comboId) != null)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的预约套餐");
        }
        BookingStatus bookingStatus = bookingInfoDto.getBookingStatus();
        if (bookingStatus == null || bookingStatus == BookingStatus.Unknown) {
            bookingStatus = BookingStatus.Processing;
        }
        if (bookingStatus == BookingStatus.Completed) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "体检已结束");
        }
        List<Booking> bookings = bookingDao.getAll(hospitalId, profileId);
        long bookingId = bookingInfoDto.getBookingId();
        if (bookings.stream().noneMatch(booking -> booking.getBookingId() == bookingInfoDto.getBookingId())) {
            bookingId = genBookingId(bookings);
        }
        // todo why == 对无效预约要不要有删除逻辑，待确认
        int processingBookingCount = (int) bookings.stream().filter(booking -> booking.getBookingStatus() == BookingStatus.Processing.getValue()).count();
        if (bookingId != bookingInfoDto.getBookingId() && processingBookingCount >= DataConst.USER_BOOKING_NUMBER_LIMIT) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "预约数超上限");
        }
        Booking newBooking = new Booking(hospitalId,
                profileId,
                bookingId,
                TimeUtil.toDefaultDate(bookingDateTime),
                dumpComboIds(new ArrayList<>(comboIds)),
                bookingStatus.getValue());
        bookingDao.replace(newBooking);
        return new AddWXBookingRspDto();
    }

    private void checkUserProfile(long userId, int hospitalId, long profileId) {
        UserProfile userProfile = userProfileDao.get(userId, hospitalId, profileId);
        if (userProfile == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的档案");
        }
    }

    private long genBookingId(List<Booking> bookings) {
        // TODO(max) 替换为全局统一id
        return bookings.stream().mapToLong(Booking::getBookingId).max().orElse(0) + 1;
    }

    @Override
    public CompleteWXBookingRspDto completeBooking(CompleteWXBookingReqDto completeWXBookingReqDto) {
        long userId = completeWXBookingReqDto.getUserId();
        long profileId = completeWXBookingReqDto.getProfileId();
        int hospitalId = completeWXBookingReqDto.getHospitalId();
        checkUserProfile(userId, hospitalId, profileId);
        long bookingId = completeWXBookingReqDto.getBookingId();
        Booking booking = bookingDao.get(hospitalId, profileId, bookingId);
        if (booking == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的预约");
        }
        if (booking.getBookingStatus() == BookingStatus.Completed.getValue()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "该体检已完成");
        }
        List<DepartmentItems> departmentItemsList = departmentItemsDao.getAll(hospitalId, profileId, bookingId);
        // todo why == 需要添加配置检查，保证同一医院的两科室不会有重复的itemId
        Set<Integer> completedItemIds = departmentItemsList.stream()
                .map(departmentItems -> loadItemValues(departmentItems.getItemValues()).keySet())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        Set<Integer> needCompleteItemIds = calcComboItemIds(loadComboIds(booking.getComboIds()));
        if (!Sets.difference(needCompleteItemIds, completedItemIds).isEmpty()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "存在未完成的体检指标项");
        }
        booking.setBookingStatus(BookingStatus.Completed.getValue());
        bookingDao.replace(booking);
        return new CompleteWXBookingRspDto();
    }

    @Override
    public GetWXBookingsRspDto getBookings(GetWXBookingsReqDto getWXBookingsReqDto) {
        long userId = getWXBookingsReqDto.getUserId();
        long profileId = getWXBookingsReqDto.getProfileId();
        int hospitalId = getWXBookingsReqDto.getHospitalId();
        // todo why 要不使用医院id和profileId来查，要不直接使用用户来
        List<Booking> bookings;
        if (HospitalConfig.get().get(hospitalId) != null) {
            bookings = getBookingsByHospitalIdAndProfileId(userId, profileId, hospitalId);
        } else {
            bookings = getBookingsByUserId(userId);
        }
        GetWXBookingsRspDto rspDto = new GetWXBookingsRspDto();
        List<BookingInfoDto> bookingInfoDtos = bookings.stream().map(this::transBriefBookingInfoDto).collect(Collectors.toList());
        rspDto.setBookings(bookingInfoDtos);
        return rspDto;
    }

    private List<Booking> getBookingsByUserId(long userId) {
        return bookingDao.getAllByUserId(userId);
    }

    private List<Booking> getBookingsByHospitalIdAndProfileId(long userId, long profileId, int hospitalId) {
        checkUserProfile(userId, hospitalId, profileId);
        return bookingDao.getAll(hospitalId, profileId);
    }

    private BookingInfoDto transBriefBookingInfoDto(Booking booking) {
        ComboConfig comboConfig = ComboConfig.get();
        List<ComboDto> comboDtos = loadComboIds(booking.getComboIds())
                .stream()
                .map(comboId -> new ComboDto(comboId, comboConfig.get(comboId).getComboName()))
                .collect(Collectors.toList());
        BookingInfoDto bookingInfoDto = new BookingInfoDto();
        bookingInfoDto.setBookingId(booking.getBookingId());
        bookingInfoDto.setBookingStatus(BookingStatus.valueOf(booking.getBookingStatus()));
        bookingInfoDto.setComboDtos(comboDtos);
        bookingInfoDto.setDateTime(booking.getDateTime());
        return bookingInfoDto;
    }

    private List<Integer> loadComboIds(String comboIds) {
        return JsonUtil.loadIntegerList(comboIds);
    }

    private String dumpComboIds(List<Integer> comboIds) {
        return JsonUtil.dumpIntegerList(comboIds);
    }

    @Override
    public GetWXBookingDetailRspDto getBookingDetail(GetWXBookingDetailReqDto getWXBookingDetailReqDto) {
        long userId = getWXBookingDetailReqDto.getUserId();
        long profileId = getWXBookingDetailReqDto.getProfileId();
        int hospitalId = getWXBookingDetailReqDto.getHospitalId();
        checkUserProfile(userId, hospitalId, profileId);
        long bookingId = getWXBookingDetailReqDto.getBookingId();
        Booking booking = bookingDao.get(hospitalId, profileId, bookingId);
        if (booking == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的预约");
        }
        List<DepartmentItems> departmentItemsList = departmentItemsDao.getAll(hospitalId, profileId, bookingId);
        BookingInfoDto bookingInfoDto = transDetailBookingInfoDto(booking, departmentItemsList);
        GetWXBookingDetailRspDto rsp = new GetWXBookingDetailRspDto();
        rsp.setBooking(bookingInfoDto);
        return rsp;
    }

    private BookingInfoDto transDetailBookingInfoDto(Booking booking, List<DepartmentItems> departmentItemsList) {
        List<ItemValueDto> itemValueDtoList = departmentItemsList.stream().map(this::transItemValueDtos).flatMap(List::stream).collect(Collectors.toList());
        BookingInfoDto bookingInfoDto = transBriefBookingInfoDto(booking);
        bookingInfoDto.setItemValueDtos(itemValueDtoList);
        return bookingInfoDto;
    }

    @Override
    public List<DepartmentDto> getDepartments(GetWXDepartmentsReqDto getWXDepartmentsReqDto){
        // todo why == 未实现接口，暂时保留，希望让前端改从配置数据接口拉取数据，需要和前端协商
        // booking -> combo -> item -> department
        throw new CollectorException(RestCode.PARAM_INVALID_ERR, "开发中");
    }

    @Override
    public List<ItemValueDto> getItems(GetWXItemsReqDto getWXItemsReqDto) {
        // 根据预约id和科室id，获取对应体检项详情
        long userId = getWXItemsReqDto.getUserId();
        int hospitalId = getWXItemsReqDto.getHospitalId();
        long profileId = getWXItemsReqDto.getProfileId();
        checkUserProfile(userId, hospitalId, profileId);
        long bookingId = getWXItemsReqDto.getBookingId();
        int departmentId = getWXItemsReqDto.getDepartmentId();
        DepartmentItems departmentItems = departmentItemsDao.get(hospitalId, profileId, bookingId, departmentId);
        if (departmentItems == null) {
            return Collections.emptyList();
        }
        return transItemValueDtos(departmentItems);
    }

    private List<ItemValueDto> transItemValueDtos(DepartmentItems departmentItems) {
        return loadItemValues(departmentItems.getItemValues()).entrySet().stream().map(entry -> {
            ItemValueDto itemValueDto = new ItemValueDto();
            itemValueDto.setItemId(entry.getKey());
            itemValueDto.setValue(entry.getValue());
            return itemValueDto;
        }).collect(Collectors.toList());
    }

    @Override
    public UpsertWXItemRspDto upsertItems(UpsertWXItemReqDto upsertWXItemReqDto) {
        // 提交体检项目值
        long userId = upsertWXItemReqDto.getUserId();
        int hospitalId = upsertWXItemReqDto.getHospitalId();
        long profileId = upsertWXItemReqDto.getProfileId();
        checkUserProfile(userId, hospitalId, profileId);
        long bookingId = upsertWXItemReqDto.getBookingId();
        Booking booking = bookingDao.get(hospitalId, profileId, bookingId);
        if (booking == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "无效的预约");
        }
        if (booking.getBookingStatus() == BookingStatus.Completed.getValue()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "本次体检已结束，不能再修改");
        }
        List<ItemValueDto> itemValueDtos = upsertWXItemReqDto.getItemValueDtos();
        if (CollectionUtils.isEmpty(itemValueDtos)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "体检指标项的值不能为空");
        }
        List<Integer> comboIds = loadComboIds(booking.getComboIds());
        Set<Integer> comboItemIds = calcComboItemIds(comboIds);
        if (itemValueDtos.stream().anyMatch(itemValueDto -> !comboItemIds.contains(itemValueDto.getItemId()))) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "套餐中不包含部分指标项");
        }
        int departmentId = upsertWXItemReqDto.getDepartmentId();
        if (itemValueDtos.stream().anyMatch(itemValueDto -> ItemConfig.get().get(itemValueDto.getItemId()).getDepartmentId() != departmentId)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "科室中不包含部分指标项");
        }
        // 根据不同的指标项类型进行检查
        for (ItemValueDto itemValueDto : itemValueDtos) {
            ItemConfigData itemConfigData = ItemConfig.get().get(itemValueDto.getItemId());
            int itemType = itemConfigData.getItemType();
            IItemHandler<?> itemHandler = itemHandlerManager.getItemHandler(itemType);
            itemHandler.checkValue(itemConfigData, itemValueDto.getValue());
        }
        String itemValues = dumpItemValues(itemValueDtos.stream().collect(Collectors.toMap(ItemValueDto::getItemId, ItemValueDto::getValue)));
        departmentItemsDao.replace(new DepartmentItems(hospitalId, profileId, bookingId, departmentId, itemValues));
        return new UpsertWXItemRspDto();
    }

    private Set<Integer> calcComboItemIds(List<Integer> comboIds) {
        ComboConfig comboConfig = ComboConfig.get();
        return comboIds.stream()
                .map(comboId -> comboConfig.get(comboId).getItemIds())
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    private Map<Integer, String> loadItemValues(String itemValues) {
        if (StringUtils.isEmpty(itemValues)) {
            return new HashMap<>();
        }
        return JSON.parseObject(itemValues, new TypeReference<Map<Integer, String>>(){}.getType());
    }

    private String dumpItemValues(Map<Integer, String> itemValues) {
        return JSON.toJSONString(itemValues);
    }

    @Override
    public GetHospitalConfigRspDto getHospitalConfig(GetHospitalConfigReqDto getHospitalConfigReqDto) {
        List<Integer> hospitalIds = getHospitalConfigReqDto.getHospitalIds();
        HospitalConfig hospitalConfig = HospitalConfig.get();
        List<HospitalConfigData> configDataList;
        if (CollectionUtils.isEmpty(hospitalIds)) {
            configDataList = hospitalConfig.getDataList();
        } else {
            configDataList = hospitalIds.stream().map(hospitalConfig::get).filter(Objects::nonNull).collect(Collectors.toList());
        }
        GetHospitalConfigRspDto rsp = new GetHospitalConfigRspDto();
        rsp.setHospitalConfigDataList(configDataList);
        return rsp;
    }

    @Override
    public GetDepartmentConfigRspDto getDepartmentConfig(GetDepartmentConfigReqDto getDepartmentConfigReqDto) {
        List<Integer> departmentIds = getDepartmentConfigReqDto.getDepartmentIds();
        if (CollectionUtils.isEmpty(departmentIds)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "科室Id不能为空");
        }
        DepartmentConfig departmentConfig = DepartmentConfig.get();
        List<DepartmentConfigData> configDataList = departmentIds.stream().map(departmentConfig::get).filter(Objects::nonNull).collect(Collectors.toList());
        GetDepartmentConfigRspDto rsp = new GetDepartmentConfigRspDto();
        rsp.setDepartmentConfigDataList(configDataList);
        return rsp;
    }

    @Override
    public GetComboConfigRspDto getComboConfig(GetComboConfigReqDto getComboConfigReqDto) {
        List<Integer> comboIds = getComboConfigReqDto.getComboIds();
        if (CollectionUtils.isEmpty(comboIds)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "套餐Id不能为空");
        }
        ComboConfig comboConfig = ComboConfig.get();
        List<ComboConfigData> configDataList = comboIds.stream().map(comboConfig::get).filter(Objects::nonNull).collect(Collectors.toList());
        GetComboConfigRspDto rsp = new GetComboConfigRspDto();
        rsp.setComboConfigDataList(configDataList);
        return rsp;
    }

    @Override
    public GetItemConfigRspDto getItemConfig(GetItemConfigReqDto getItemConfigReqDto) {
        List<Integer> itemIds = getItemConfigReqDto.getItemIds();
        if (CollectionUtils.isEmpty(itemIds)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "指标项Id不能为空");
        }
        ItemConfig itemConfig = ItemConfig.get();
        List<ItemConfigData> configDataList = itemIds.stream().map(itemConfig::get).filter(Objects::nonNull).collect(Collectors.toList());
        List<Object> itemExtConfigDataList = configDataList.stream()
                .map(itemConfigData -> Pair.of(itemConfigData.getItemType(), itemConfigData.getItemTypeId()))
                .collect(Collectors.toSet())
                .stream()
                .map(key -> {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(itemHandlerManager.getItemHandler(key.getLeft()).getConfigData(key.getRight()));
                    jsonObject.put("itemType", key.getLeft());
                    return jsonObject;
                })
                .collect(Collectors.toList());
        
        GetItemConfigRspDto rsp = new GetItemConfigRspDto();
        rsp.setItemConfigDataList(configDataList);
        rsp.setItemExtConfigDataList(itemExtConfigDataList);
        return rsp;
    }

    /** todo why == 因为不会用aop~先直接套一层函数来鉴权 */
    @Override
    public <RES, REQ extends BaseDto> RestResponse<RES> authentication(REQ reqDto, Supplier<RES> resDtoSupplier) {
        // todo why == 应该改用sessionId的
        if (!userRedisDao.isExistOpenId(reqDto.getUserId(), reqDto.getOpenId())) {
            throw new CollectorException(RestCode.INVALID_OPEN_ID_OR_USER_ID_ERR, "无效的openId或userId");
        }
        // 校验通过，延长过期时间
        userRedisDao.expireOpenId(reqDto.getUserId(), reqDto.getOpenId());
        return new RestResponse<>(resDtoSupplier.get());
    }
}
