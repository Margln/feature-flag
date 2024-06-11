package com.xy.feature.msg;


/**
 * @author margln
 * @date 2024/4/17
 */
public class ObjectMsg extends Message<Object>{

    public ObjectMsg(int type, Object data) {
        super(type, data);
    }
}
