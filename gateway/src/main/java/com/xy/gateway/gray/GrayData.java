package com.xy.gateway.gray;

import com.xy.gateway.UserContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author margln
 * @date 2024/4/17
 */
public class GrayData {

    private static Map<String, Object> grayData = new ConcurrentHashMap<>();

    public static boolean isGray(String feature, boolean defaultVal) {
        Object v = grayData.get(feature);
        if (v == null) {
            return defaultVal;
        }
        return UserContext.getUserInfo().get("lesseeId").equals(v);
    }



    public static boolean isGray(String feature, Object data, boolean defaultVal) {
        Object v = grayData.get(feature);
        if (v == null) {
            return defaultVal;
        }
        return v.equals(data);
    }

    public static boolean isPrd(String feature, boolean defaultVal) {

        Object v = grayData.get(feature);
        if (v == null) {
            return defaultVal;
        }
        return UserContext.getUserInfo().get("userNo").equals(v);
    }
}
