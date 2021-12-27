package com.metahospital.datacollector.controller.dto;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;

@Data
public class BookingInfoDto {
    private long bookingId = -1; // 写操作自行生成
    private DateTime dateTime;
    private List<ComboDto> comboDtos; // 套餐，一次预约可有多个套餐
}
