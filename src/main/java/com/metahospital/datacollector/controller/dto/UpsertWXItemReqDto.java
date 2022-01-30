package com.metahospital.datacollector.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * 本请求中的userId是医生账号的userId，而hospitalId,profileId,bookingId是体检人的预约信息
 */
@Data
public class UpsertWXItemReqDto extends BaseDto {
    /** 医院id */
    private int hospitalId;
    /** 档案id */
    private long profileId;
    /** 预约Id */
    private long bookingId;
    /** 科室Id */
    private int departmentId;
    /** 指标项值列表，需要包含该科室所有指标项值，不能将各指标项分开成多次提交 */
    private List<ItemValueDto> itemValueDtos;
}
