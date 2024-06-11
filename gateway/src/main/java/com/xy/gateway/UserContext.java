package com.xy.gateway;

import java.util.Map;

/**
 * @author margln
 * @date 2024/4/17
 */
public class UserContext {


    private static ThreadLocal<Map<String, Object>> userInfo = new ThreadLocal<>();


    public static void setUserInfo(Map<String, Object> user) {
        userInfo.set(user);
    }


    public static Map<String, Object> getUserInfo() {
        return userInfo.get();
    }

    public static void removeUserInfo() {
        userInfo.remove();
    }


    public static void clear() {
        userInfo.remove();
    }
}
