package com.xy.feature.msg;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author margln
 * @date 2024/4/17
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class StringMsg extends Message<String> {

    private static  StringMsg PING = new StringMsg(MsgType.PING, "ping");

    private static  StringMsg INIT = new StringMsg(MsgType.INIT, "init");

    public StringMsg(int type, String data) {
        super(type, data);
    }

    public static StringMsg getPingMsg(String appKey) {
        PING.setAppKey(appKey);
        return PING;
    }

    public static StringMsg getInitMsg(String appKey) {
        INIT.setAppKey(appKey);
        return INIT;
    }

}
