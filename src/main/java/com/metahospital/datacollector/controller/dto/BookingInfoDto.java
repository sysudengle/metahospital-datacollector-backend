package com.metahospital.datacollector.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metahospital.datacollector.common.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BookingInfoDto {
    private long bookingId = -1; // 写操作自行生成
    // TOREVIEW 原来DateTimeFormat不能用于dto返回值
    @JsonFormat(pattern = "yyyy-MM-dd")  //这个格式可能要定一下。
    private Date dateTime;
    private List<ComboDto> comboDtos; // 套餐，一次预约可有多个套餐
    private BookingStatus bookingStatus = BookingStatus.Unknown; //booking 状态

    public BookingInfoDto(long bookingId, Date dateTime, List<ComboDto> comboDtos) {
        this.bookingId = bookingId;
        this.dateTime = dateTime;
        this.comboDtos = comboDtos;
        this.bookingStatus = BookingStatus.Processing;
    }

    public BookingInfoDto(long bookingId, Date dateTime, List<ComboDto> comboDtos, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.dateTime = dateTime;
        this.comboDtos = comboDtos;
        this.bookingStatus = bookingStatus;
    }
}
