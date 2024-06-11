package com.xy.feature.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author margln
 * @date 2024/3/27
 */
@Slf4j
@Component
@Order(value = 100)
public class FeatureServer {

    @Resource
    NettyServer nettyServer;

    @PostConstruct
    public void start() {
        log.info("FeatureServer start!");
        new Thread(nettyServer).start();
    }


    @PreDestroy
    public void stop() {
        log.info("FeatureServer stop!");
        nettyServer.stop();
    }


}
