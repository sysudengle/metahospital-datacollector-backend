package com.metahospital.datacollector.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.google.common.io.Resources;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author wanghaoyuan
 */
public class ConfigUtil {
     public static <T> T loadDataList(Class configClass, Type type) {
        String fileName = "configs/" + configClass.getSimpleName() + ".json";
        String jsonString = "";
        try {
            jsonString = Resources.toString(Resources.getResource(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return JSON.parseObject(jsonString, type, new ParserConfig(true));
    }
}
