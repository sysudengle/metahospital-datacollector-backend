package com.metahospital.datacollector.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class BookingInfoDto {
    private long bookingId = -1; // 写操作自行生成
    @DateTimeFormat(pattern = "yyyy-MM-dd")  //这个格式可能要定一下。
    private Date dateTime;
    private List<ComboDto> comboDtos; // 套餐，一次预约可有多个套餐
}
