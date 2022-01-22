package com.metahospital.datacollector.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetWXBookingsRspDto {
    /** bookings 里该有status */
    private List<BookingInfoDto> bookings;
}
