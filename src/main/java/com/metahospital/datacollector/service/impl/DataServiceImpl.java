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
        // ????????????openId??????
        JSONObject wxJsonRsp = WechatUtil.getSessionKeyOrOpenId(authReqDto.getWechatJsCode());
        String openId = wxJsonRsp.getString("openid");
        if (StringUtils.isEmpty(openId)) {
            throw new CollectorException(RestCode.WECHAT_OPENID_INVALID_ERR, "????????????openId??????");
        }
        WechatAccount wxAccount = wechatAccountDao.get(openId);
        if (wxAccount != null) {
            LOGGER.info("auth hit db!");
            // ????????????openid??????userid ???????????????userid??????usertype??????????????????????????????????????????????????????????????????
            User user = userDao.get(wxAccount.getUserId());
            userRedisDao.setOpenId(user, openId);
            return genAuthRsp(user, loadValidDoctorInfoDto(user), openId);
        }
        // ???????????????????????????????????? 
        // TODO(max) ????????????db?????????????????????????????????
        long newUserId = genUserId();
        // ????????????null??????????????????db????????????
        String unionId = Optional.ofNullable(wxJsonRsp.getString("unionid")).orElse("");
        String sessionKey = Optional.ofNullable(wxJsonRsp.getString("session_key")).orElse("");
        // db??????
        User newUser = new User(newUserId, "lee", UserType.Patient.getValue());
        userDao.replace(newUser);
        wechatAccountDao.replace(new WechatAccount(openId, unionId, sessionKey, newUserId));
        // redis?????????????????????
        userRedisDao.setOpenId(newUser, openId);
        return genAuthRsp(newUser, loadValidDoctorInfoDto(newUser), openId);
    }

    private long genUserId() {
        // todo why == ??????????????????????????????????????????????????????id????????????
        return System.nanoTime();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????null
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????Id");
        }
        long userId = registerWXDoctorReqDto.getUserId();
        UserDoctor userDoctor = userDoctorDao.get(userId);
        // REVIEWED ?????????????????????,??????????????????????????????????????????????????????????????????
        if (userDoctor != null) {
            throw new CollectorException(RestCode.DOCTOR_ALREADY_REGISTER_ERR, "?????????????????????");
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????Id");
        }
        if (!checkPersonalID(profileInfoDto.getPersonalID())) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "????????????????????????");
        }
        if (profileInfoDto.getGender() == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????????????????");
        }
        StringUtil.checkStringNotEmptyWithLengthLimit(profileInfoDto.getFullName(), DataConst.FULL_NAME_LENGTH_LIMIT, "??????");
        StringUtil.checkStringNotEmptyWithLengthLimit(profileInfoDto.getPidAddress(), DataConst.PID_ADDRESS_LENGTH_LIMIT, "???????????????");
        StringUtil.checkStringNotEmptyWithLengthLimit(profileInfoDto.getHomeAddress(), DataConst.HOME_ADDRESS_LENGTH_LIMIT, "????????????");
        Profile oldProfile = profileDao.getByPersonalID(configData.getHospitalId(), profileInfoDto.getPersonalID());
        if (oldProfile != null && oldProfile.getProfileId() != profileInfoDto.getProfileId()) {
            // ???????????????????????????????????????????????????
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????????????????????????????????????????????????????");
        }
        List<UserProfile> userProfiles = userProfileDao.getAll(userId);
        long profileId = profileInfoDto.getProfileId();
        if (userProfiles.stream().noneMatch(userProfile -> userProfile.getProfileId() == profileInfoDto.getProfileId())) {
            profileId = genProfileId(userProfiles);
        }
        if (profileId != profileInfoDto.getProfileId() && userProfiles.size() >= DataConst.USER_PROFILE_NUMBER_LIMIT) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("???????????????????????????????????????%s", DataConst.USER_PROFILE_NUMBER_LIMIT));
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
        // REVIEWED a) ?????????????????????????????? b) ????????????db???????????????????????????
        userProfileDao.replace(newUserProfile);
        profileDao.replace(newProfile);
        return new AddWXProfileRspDto(transProfileInfo(newProfile));
    }

    private boolean checkPersonalID(String personalID) {
        // todo why == ?????????????????????
        if (StringUtils.isEmpty(personalID)) {
            return false;
        }
        return true;
    }

    private long genProfileId(List<UserProfile> userProfiles) {
        // TODO(max) ?????????????????????id
        return System.nanoTime();
    }

    @Override
    public GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto) {
        // todo why == ????????????profile?????????????????????????????????userProfile???????????????????????????????????????????????????????????????????????????
        // TOREVIEW ????????????db?????????????????????????????????
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
        // TOREVIEW ???????????????????????????????????????????????????????????????????????????????????????db????????????db???
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
     * TOREVIEW ??????????????????????????????????????????upsert?????????update/insert?????????
     */
    // 
    @Override
    public AddWXBookingRspDto upsertBooking(AddWXBookingReqDto addWXBookingReqDto) {
        long userId = addWXBookingReqDto.getUserId();
        long profileId = addWXBookingReqDto.getProfileId();
        int hospitalId = addWXBookingReqDto.getHospitalId();
        BookingInfoDto bookingInfoDto = addWXBookingReqDto.getBookingInfoDto();
        if (bookingInfoDto == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????????????????");
        }
        checkUserProfile(userId, hospitalId, profileId);
        Date dateTime = bookingInfoDto.getDateTime();
        if (dateTime == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "????????????????????????");
        }
        LocalDate bookingDateTime = TimeUtil.toDefaultLocalDate(dateTime);
        if (bookingDateTime.isBefore(LocalDate.now())) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????????????????");
        }
        List<ComboDto> comboDtos = bookingInfoDto.getComboDtos();
        if (CollectionUtils.isEmpty(comboDtos)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "????????????????????????");
        }
        Set<Integer> comboIds = comboDtos.stream().mapToInt(ComboDto::getComboId).boxed().collect(Collectors.toSet());
        ComboConfig comboConfig = ComboConfig.get();
        if (!comboIds.stream().allMatch(comboId -> comboConfig.get(comboId) != null)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????????????????");
        }
        BookingStatus bookingStatus = bookingInfoDto.getBookingStatus();
        if (bookingStatus == null || bookingStatus == BookingStatus.Unknown) {
            bookingStatus = BookingStatus.Processing;
        }
        if (bookingStatus == BookingStatus.Completed) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????");
        }
        List<Booking> bookings = bookingDao.getAll(hospitalId, profileId);
        long bookingId = bookingInfoDto.getBookingId();
        if (bookings.stream().noneMatch(booking -> booking.getBookingId() == bookingInfoDto.getBookingId())) {
            bookingId = genBookingId(bookings);
        }
        // todo why == ???????????????????????????????????????????????????
        int processingBookingCount = (int) bookings.stream().filter(booking -> booking.getBookingStatus() == BookingStatus.Processing.getValue()).count();
        if (bookingId != bookingInfoDto.getBookingId() && processingBookingCount >= DataConst.USER_BOOKING_NUMBER_LIMIT) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????????????????");
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????");
        }
    }

    private long genBookingId(List<Booking> bookings) {
        // TODO(max) ?????????????????????id
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????");
        }
        if (booking.getBookingStatus() == BookingStatus.Completed.getValue()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????????????????");
        }
        List<DepartmentItems> departmentItemsList = departmentItemsDao.getAll(hospitalId, profileId, bookingId);
        // todo why == ???????????????????????????????????????????????????????????????????????????itemId
        Set<Integer> completedItemIds = departmentItemsList.stream()
                .map(departmentItems -> loadItemValues(departmentItems.getItemValues()).keySet())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        Set<Integer> needCompleteItemIds = calcComboItemIds(loadComboIds(booking.getComboIds()));
        if (!Sets.difference(needCompleteItemIds, completedItemIds).isEmpty()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????????????????????????????");
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
        // todo why ??????????????????id???profileId????????????????????????????????????
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????");
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
        // todo why == ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // booking -> combo -> item -> department
        throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????");
    }

    @Override
    public List<ItemValueDto> getItems(GetWXItemsReqDto getWXItemsReqDto) {
        // ????????????id?????????id??????????????????????????????
        long userId = getWXItemsReqDto.getUserId();
        int hospitalId = getWXItemsReqDto.getHospitalId();
        long profileId = getWXItemsReqDto.getProfileId();
        checkUserProfile(userId, hospitalId, profileId);
        long bookingId = getWXItemsReqDto.getBookingId();
        Booking booking = bookingDao.get(hospitalId, profileId, bookingId);
        if (booking == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????");
        }
        List<Integer> comboIds = loadComboIds(booking.getComboIds());
        int departmentId = getWXItemsReqDto.getDepartmentId();
        Set<Integer> comboDepartmentItemIds = calcComboDepartmentItemIds(comboIds, departmentId);
        DepartmentItems departmentItems = departmentItemsDao.get(hospitalId, profileId, bookingId, departmentId);
        List<ItemValueDto> itemValueDtos = departmentItems == null ? Collections.emptyList() : transItemValueDtos(departmentItems);
        return fillItemValueDtosWithEmptyValue(itemValueDtos, comboDepartmentItemIds);
    }

    private Set<Integer> calcComboDepartmentItemIds(List<Integer> comboIds, int departmentId) {
        List<Integer> itemIds = comboIds.stream()
                .map(comboId -> ComboConfig.get().get(comboId))
                .map(ComboConfigData::getItemIds)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        DepartmentConfigData departmentConfigData = DepartmentConfig.get().get(departmentId);
        if (departmentConfigData == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????");
        }
        return departmentConfigData.getItemIds().stream().filter(itemIds::contains).collect(Collectors.toSet());
    }

    private List<ItemValueDto> fillItemValueDtosWithEmptyValue(List<ItemValueDto> itemValueDtos, Set<Integer> comboDepartmentItemIds) {
        List<ItemValueDto> result = new ArrayList<>();
        Set<Integer> itemIds = itemValueDtos.stream().map(ItemValueDto::getItemId).collect(Collectors.toSet());
        List<ItemValueDto> emptyItemValueDtos = comboDepartmentItemIds.stream().filter(itemId -> !itemIds.contains(itemId)).map(itemId -> {
            ItemValueDto itemValueDto = new ItemValueDto();
            itemValueDto.setItemId(itemId);
            itemValueDto.setValue("");
            return itemValueDto;
        }).collect(Collectors.toList());
        result.addAll(itemValueDtos);
        result.addAll(emptyItemValueDtos);
        return result;
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
        // ?????????????????????
        long userId = upsertWXItemReqDto.getUserId();
        int hospitalId = upsertWXItemReqDto.getHospitalId();
        long profileId = upsertWXItemReqDto.getProfileId();
        int departmentId = upsertWXItemReqDto.getDepartmentId();
        checkUserDoctor(userId, hospitalId, departmentId);
        long bookingId = upsertWXItemReqDto.getBookingId();
        Booking booking = bookingDao.get(hospitalId, profileId, bookingId);
        if (booking == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????");
        }
        if (booking.getBookingStatus() == BookingStatus.Completed.getValue()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "???????????????????????????????????????");
        }
        List<ItemValueDto> itemValueDtos = upsertWXItemReqDto.getItemValueDtos();
        if (CollectionUtils.isEmpty(itemValueDtos)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????????????????????????????");
        }
        List<Integer> comboIds = loadComboIds(booking.getComboIds());
        Set<Integer> comboDepartmentItemIds = calcComboDepartmentItemIds(comboIds, departmentId);
        Set<Integer> itemIds = itemValueDtos.stream().map(ItemValueDto::getItemId).collect(Collectors.toSet());
        if (itemIds.size() != comboDepartmentItemIds.size() || itemIds.stream().anyMatch(itemId -> !comboDepartmentItemIds.contains(itemId))) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????????????????????????????????????????????????????");
        }
        // ??????????????????????????????????????????
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

    private void checkUserDoctor(long userId, int hospitalId, int departmentId) {
        UserDoctor userDoctor = userDoctorDao.get(userId);
        if (userDoctor == null) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????????????????");
        }
        if (userDoctor.getHospitalId() != hospitalId) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????????????????????????????");
        }
        if (!JsonUtil.loadIntegerList(userDoctor.getDepartmentIds()).contains(departmentId)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????????????????????????????");
        }
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????Id????????????");
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "??????Id????????????");
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
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, "?????????Id????????????");
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

    /** todo why == ???????????????aop~????????????????????????????????? */
    @Override
    public <RES, REQ extends BaseDto> RestResponse<RES> authentication(REQ reqDto, Supplier<RES> resDtoSupplier) {
        // todo why == ????????????sessionId???
        if (!userRedisDao.isExistOpenId(reqDto.getUserId(), reqDto.getOpenId())) {
            throw new CollectorException(RestCode.INVALID_OPEN_ID_OR_USER_ID_ERR, "?????????openId???userId");
        }
        // ?????????????????????????????????
        userRedisDao.expireOpenId(reqDto.getUserId(), reqDto.getOpenId());
        return new RestResponse<>(resDtoSupplier.get());
    }
}
