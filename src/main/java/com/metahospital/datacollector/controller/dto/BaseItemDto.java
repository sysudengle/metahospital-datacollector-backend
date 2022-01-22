package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.ItemType;
import com.metahospital.datacollector.config.configs.ItemConfigData;
import lombok.Data;

/**
 * todo why == 目前没有用到该结构，而用了{@link ItemConfigData}，先实现功能，待商量确认
 * 体检指标项，如身高、体重等
 */
@Data
public class BaseItemDto {
    /** 指标数据的id */
    protected int itemId;
    /** 体检项目名 */
    protected String name;
    /** ？医生录入的数据得再做一个表，这里可以暂时用随机数 */
    protected String value;
    /** 指标的描述 */
    protected String description;
    /** 指标数据类型，比如是下拉列表还是字符串 */
    protected ItemType itemType;
}
