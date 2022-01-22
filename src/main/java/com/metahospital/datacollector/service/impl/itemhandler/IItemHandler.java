package com.metahospital.datacollector.service.impl.itemhandler;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.config.configs.ItemConfigData;

public interface IItemHandler<T> {
    /** 返回指标项类型 */
    int getItemType();
    /** 返回对应itemTypeId的配置数据 */
    T getConfigData(int itemTypeId);

    void checkValue(ItemConfigData itemConfigData, String value) throws CollectorException;
}
