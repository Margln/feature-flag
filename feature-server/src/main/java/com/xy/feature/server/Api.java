package com.xy.feature.server;

import com.xy.feature.msg.MessageProtocol;
import com.xy.feature.msg.ObjectMsg;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.SocketAddress;
import java.util.Map;

/**
 * @author margln
 * @date 2024/4/17
 */
@Slf4j
@RestController
@RequestMapping("/feature")
public class Api {

    @Resource(name = "appIpChannelMap")
    Map<String, Map<SocketAddress, Channel>> appIpChannelMap;


    @RequestMapping("update")
    public String update() {

        Map<SocketAddress, Channel> testapp = appIpChannelMap.get("testapp");
        testapp.forEach((k,v) -> {
            log.info("update testapp feature .... ");
            v.writeAndFlush(new MessageProtocol(new ObjectMsg(2, "update feature")));
        });

        return "ok";
    }


}
