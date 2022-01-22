package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.config.configs.ItemConfigData;
import com.metahospital.datacollector.config.configs.NumberItemConfigData;
import com.metahospital.datacollector.config.configs.SelectionItemConfigData;
import com.metahospital.datacollector.config.configs.StringItemConfigData;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetItemConfigRspDto {
    /** todo why == 应该要使用dto数据结构 */
    private List<ItemConfigData> itemConfigDataList;
    /**
     * itemId -> 扩展配置数据 <br>
     * 扩展配置数据类型根据itemId对应的itemType不同，如下： <br>
     * 1:数值类型扩展配置数据 {@link NumberItemConfigData} <br>
     * 2:字符类型扩展配置数据 {@link StringItemConfigData} <br>
     * 3:选择列表类型扩展配置数据 {@link SelectionItemConfigData} <br>
     **/
    private Map<Integer, Object> itemExtConfigData;
}
