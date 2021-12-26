package com.metahospital.datacollector.controller.dto;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class BookingInfoDto {
    private long bookingId = -1; // 写操作自行生成
    private DateTime dateTime;
}
