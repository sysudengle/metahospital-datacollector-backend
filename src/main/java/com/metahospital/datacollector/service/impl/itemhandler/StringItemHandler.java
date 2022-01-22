package com.metahospital.datacollector.service.impl.itemhandler;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.common.enums.ItemType;
import com.metahospital.datacollector.common.util.StringUtil;
import com.metahospital.datacollector.config.configs.ItemConfigData;
import com.metahospital.datacollector.config.configs.NumberItemConfig;
import com.metahospital.datacollector.config.configs.NumberItemConfigData;
import com.metahospital.datacollector.config.configs.StringItemConfig;
import com.metahospital.datacollector.config.configs.StringItemConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author wanghaoyuan
 */
@Component
public class StringItemHandler implements IItemHandler<StringItemConfigData> {
    public static final Logger LOGGER = LoggerFactory.getLogger(StringItemHandler.class);
    
    @Override
    public int getItemType() {
        return ItemType.StringType.getValue();
    }

    @Override
    public StringItemConfigData getConfigData(int itemTypeId) {
        return StringItemConfig.get().get(itemTypeId);
    }

    @Override
    public void checkValue(ItemConfigData itemConfigData, String value) throws CollectorException {
        StringItemConfigData configData = getConfigData(itemConfigData.getItemTypeId());
        StringUtil.checkStringNotEmptyWithLengthLimit(value, configData.getMaxLength(), itemConfigData.getItemName());
    }
}
