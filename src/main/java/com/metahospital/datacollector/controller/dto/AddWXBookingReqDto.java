package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.BookingInfo;
import lombok.Data;

@Data
public class AddWXBookingReqDto extends BaseDto {
    private int bookingId;
    private BookingInfo bookingInfo;
}
