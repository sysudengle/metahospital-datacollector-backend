package com.metahospital.datacollector.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetWXBookingDetailRspDto {
    /** 体检预约详细信息 */
    private BookingInfoDto booking;
}
