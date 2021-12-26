package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.AppointmentInfo;
import lombok.Data;

@Data
public class AddAppointmentReqDto extends BaseDto {
    private int dossierId;
    private AppointmentInfo appointmentInfo;
}
