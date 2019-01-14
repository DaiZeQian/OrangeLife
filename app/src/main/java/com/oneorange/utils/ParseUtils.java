package com.oneorange.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2015/11/30.
 */
public class ParseUtils {
    public static <T> T getObject(String jsonString, Class<T> cls) {
        return JSON.parseObject(jsonString, cls);
    }

    public static <T> List<T> getObjects(String jsonString, Class<T> cls) {
        return JSON.parseArray(jsonString, cls);
    }

    public static List<Map<String, String>> getKeyMapsList(String jsonString) {
        List<Map<String, String>> list;
        list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, String>>>() {
        });
        return list;
    }
}
