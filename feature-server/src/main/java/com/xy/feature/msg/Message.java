package com.xy.feature.msg;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author margln
 * @date 2024/4/17
 */
@Data
@NoArgsConstructor
public class Message<T> {

    int type;
    String appKey;
    T data;

    public Message(int type, T data) {
        this.type = type;
        this.data = data;
    }

}
