package com.xy.feature.msg;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

/**
 * @author margln
 * @date 2024/4/16
 */
@Data
@NoArgsConstructor
public class MessageProtocol {

    private int len;

    private byte[] content;


    public MessageProtocol(Message<?> msg) {
        String str = JSON.toJSONString(msg);
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        this.len = bytes.length;
        this.content = bytes;
    }
}
