package com.metahospital.datacollector.controller.dto;

import lombok.Data;

// 录入item时的值,后端操作得用序列化存储
@Data
public class WriteItemDto {
    //protected long itemScehmaId;
    private long itemId;
    private String value;
}
