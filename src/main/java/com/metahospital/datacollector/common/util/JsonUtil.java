package com.metahospital.datacollector.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static <T> List<T> loadList(String jsonIntegerArray, Class<T> clazz) {
        if (StringUtils.isNullOrEmpty(jsonIntegerArray)) {
            return new ArrayList<>();
        } else {
            return JSON.parseArray(jsonIntegerArray, clazz);
        }
    }

    public static <T> String dumpList(List<T> list) {
        return JSON.toJSONString(list);
    }
    
    public static List<Integer> loadIntegerList(String jsonIntegerArray) {
        if (StringUtils.isNullOrEmpty(jsonIntegerArray)) {
            return new ArrayList<>();
        } else {
            return JSON.parseArray(jsonIntegerArray, Integer.class);
        }
    }

    public static String dumpIntegerList(List<Integer> list) {
        return JSON.toJSONString(list);
    }
    
    public static List<List<Integer>> load2DIntegerList(String json2DIntegerArray) {
        if (StringUtils.isNullOrEmpty(json2DIntegerArray)) {
            return new ArrayList<>();
        } else {
            return JSON.parseObject(json2DIntegerArray, new TypeReference<List<List<Integer>>>(){}.getType());
        }
    }
    
    public static String dump2DIntegerList(List<List<Integer>> lists) {
        return JSON.toJSONString(lists);
    }

    public static Map<Integer, Integer> loadIntegerMap(String jsonIntegerMap) {
        if (StringUtils.isNullOrEmpty(jsonIntegerMap)) {
            return new HashMap<>();
        } else {
            return JSON.parseObject(jsonIntegerMap, new TypeReference<Map<Integer, Integer>>(){}.getType());
        }
    }
    
    public static String dumpIntegerMap(Map<Integer, Integer> map) {
        return JSON.toJSONString(map);
    }
}
