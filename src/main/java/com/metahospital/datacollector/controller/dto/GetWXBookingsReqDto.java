package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class GetWXBookingsReqDto extends BaseDto {
    /** 医院Id，当为0时，表时查询用户全部预约信息 */
    private int hospitalId;
    /** 根据档案id获取所有预约 */
    private long profileId;
}
