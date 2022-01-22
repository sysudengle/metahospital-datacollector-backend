package com.metahospital.datacollector.service.impl.itemhandler;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import com.metahospital.datacollector.common.enums.ItemType;
import com.metahospital.datacollector.common.util.JsonUtil;
import com.metahospital.datacollector.config.configs.ItemConfigData;
import com.metahospital.datacollector.config.configs.SelectionItemConfig;
import com.metahospital.datacollector.config.configs.SelectionItemConfigData;
import com.metahospital.datacollector.config.configs.customtype.SelectionItemOptionCO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wanghaoyuan
 */
@Component
public class SelectionItemHandler implements IItemHandler<SelectionItemConfigData> {
    public static final Logger LOGGER = LoggerFactory.getLogger(SelectionItemHandler.class);
    
    @Override
    public int getItemType() {
        return ItemType.SelectionType.getValue();
    }

    @Override
    public SelectionItemConfigData getConfigData(int itemTypeId) {
        return SelectionItemConfig.get().get(itemTypeId);
    }

    @Override
    public void checkValue(ItemConfigData itemConfigData, String value) throws CollectorException {
        List<Integer> v;
        try {
            v = JsonUtil.loadIntegerList(value);
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("非法数据类型,%s,%s", itemConfigData.getItemName(), value));
        }
        if (CollectionUtils.isEmpty(v)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("选项不能为空,%s,%s", itemConfigData.getItemName(), value));
        }
        if (v.size() != new HashSet<>(v).size()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("存在重复选项,%s,%s", itemConfigData.getItemName(), value));
        }
        SelectionItemConfigData configData = getConfigData(itemConfigData.getItemTypeId());
        if (configData.getSelectNum() != SelectionItemConfig.SELECT_NUM_MULTI && v.size() != configData.getSelectNum()) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("选择数量不符合配置要求,%s,%s", itemConfigData.getItemName(), value));
        }
        Set<Integer> optionValues = configData.getOptionList().stream().map(SelectionItemOptionCO::getValue).collect(Collectors.toSet());
        if (!optionValues.containsAll(v)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("存在非法选项,%s,%s", itemConfigData.getItemName(), value));
        }
    }
}
