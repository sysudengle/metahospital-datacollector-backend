package com.metahospital.datacollector.common;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class BookingInfo {
    private long bookingId = -1; // 写操作自行生成
    private DateTime dateTime;
}
