package com.xy.feature.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * @author margln
 * @date 2024/3/27
 */
@Slf4j
//@Component
@Order(value = 100)
public class FeatureServer {


    @Resource
    NettyServer nettyServer;

    @PostConstruct
    public void start() {
        log.info("FeatureServer start!");
        nettyServer.run();
    }



    @PreDestroy
    public void stop() {
        log.info("FeatureServer stop!");
        nettyServer.stop();
    }


}
