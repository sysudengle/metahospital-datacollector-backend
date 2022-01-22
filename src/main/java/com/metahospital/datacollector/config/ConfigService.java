package com.metahospital.datacollector.config;

import com.metahospital.datacollector.common.AppContext;
import com.metahospital.datacollector.config.configs.ComboConfig;
import com.metahospital.datacollector.config.configs.DepartmentConfig;
import com.metahospital.datacollector.config.configs.HospitalConfig;
import com.metahospital.datacollector.config.configs.ItemConfig;
import com.metahospital.datacollector.config.configs.NumberItemConfig;
import com.metahospital.datacollector.config.configs.SelectionItemConfig;
import com.metahospital.datacollector.config.configs.StringItemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * todo why == 因为数据库接入接口比较麻烦，所以先直接读取json文件加载配置，完善数据库接口后，再改为从数据库读取(20220121)
 * @author wanghaoyuan
 */
@Component
public class ConfigService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigService.class);
    private static List<Class<? extends IConfig>> CONFIG_CLASSES = Stream.of(
            HospitalConfig.class,
            ComboConfig.class,
            DepartmentConfig.class,
            ItemConfig.class,
            NumberItemConfig.class,
            StringItemConfig.class,
            SelectionItemConfig.class
    ).collect(Collectors.toList());
    
    private ConfigManager configManager;

    private ConfigService() {
        reload();
    }

    /**
     * 更新所有配置
     */
    public void reload() {
        ConfigManager configManager = new ConfigManager();
        configManager.init();
        this.configManager = configManager;
    }

    public static <T extends IConfig> T getConfig(Class<T> clazz) {
        return AppContext.getBean(ConfigService.class).configManager.get(clazz);
    }
    
    private static class ConfigManager {
        private Map<Class<? extends IConfig>, IConfig> configMap;

        public void init() {
            createConfigs();
            loadConfigs();
        }

        private void createConfigs() {
            Map<Class<? extends IConfig>, IConfig> map = new HashMap<>();
            for (Class<? extends IConfig> configClass : CONFIG_CLASSES) {
                IConfig config = null;
                try {
                    config = configClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error("", e);
                }
                map.put(configClass, config);
            }
            configMap = map;
        }
        
        private void loadConfigs() {
            for (IConfig config : configMap.values()) {
                config.loadJson();
            }
        }

        public <T extends IConfig> T get(Class<T> clazz) {
            return (T) configMap.get(clazz);
        }
    }
}
