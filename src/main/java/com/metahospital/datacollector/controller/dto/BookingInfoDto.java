package com.metahospital.datacollector.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metahospital.datacollector.common.enums.BookingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BookingInfoDto {
    /** 预约Id，写操作自行生成 */
    private long bookingId = -1;
    /**
     * TOREVIEW 原来DateTimeFormat不能用于dto返回值
     * 这个格式可能要定一下。
     * 预约日期，格式如yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTime;
    /** 套餐，一次预约可有多个套餐 */
    private List<ComboDto> comboDtos;
    /** booking 状态 */
    private BookingStatus bookingStatus;
    /** 各体检指标项值，在通过detail接口获取时，该字段才会有值 */
    private List<ItemValueDto> itemValueDtos;

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
