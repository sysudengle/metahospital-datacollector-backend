package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.BookingInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetWXBookingsRspDto {
    private List<BookingInfo> bookings;
}
