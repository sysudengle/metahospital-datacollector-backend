package com.metahospital.datacollector.controller.dto;

import lombok.Data;

import java.util.List;

// 保留用于后续添加字段
@Data
public class GetBookingConfigRspDto {
    private List<ComboDto> comboDtos;

}
