package com.metahospital.datacollector.service.impl.itemhandler;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.common.enums.ItemType;
import com.metahospital.datacollector.config.configs.ItemConfigData;
import com.metahospital.datacollector.config.configs.NumberItemConfig;
import com.metahospital.datacollector.config.configs.NumberItemConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wanghaoyuan
 */
@Component
public class NumberItemHandler implements IItemHandler<NumberItemConfigData> {
    public static final Logger LOGGER = LoggerFactory.getLogger(NumberItemHandler.class);
    
    @Override
    public int getItemType() {
        return ItemType.NumberType.getValue();
    }

    @Override
    public NumberItemConfigData getConfigData(int itemTypeId) {
        return NumberItemConfig.get().get(itemTypeId);
    }

    @Override
    public void checkValue(ItemConfigData itemConfigData, String value) throws CollectorException {
        long v;
        try {
            v = Long.parseLong(value);
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("非法数据类型,%s,%s", itemConfigData.getItemName(), value));
        }
        NumberItemConfigData configData = getConfigData(itemConfigData.getItemTypeId());
        if (v < configData.getMin() || v > configData.getMax()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("数值超出限定范围,%s,%s", itemConfigData.getItemName(), value));
        }
    }
}
