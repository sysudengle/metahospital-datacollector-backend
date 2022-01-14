package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.ItemType;
import lombok.Data;

// 体检指标项，如身高、体重等
@Data
public class BaseItemDto {
    // TOCHECK(浩源) 待数据库涉及确认下述设计再确认
    // protected long itemScehmaId; // 指标配置的id
    protected long itemId = -1; // 指标数据的id，未写入任何值应为默认值-1

    protected String name;
    protected String value;
    protected String description; // 指标的描述
    protected ItemType itemType;
}
