package com.metahospital.datacollector.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetWXBookingsRspDto {
    private List<BookingInfoDto> bookings;  //bookings 里该有status
}
