package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.AppointmentInfo;
import lombok.Data;

import java.util.Map;

@Data
public class GetAppointmentRspDto {
    private Map<Integer, AppointmentInfo> appointmentInfoMap;
}
