package com.xy.feature.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author margln
 * @date 2024/4/17
 */
@Component
@Order
@Slf4j
public class FeatureClient2 {

    @Resource
    NettyClient nettyClient;

    @PostConstruct
    public void start() {
        log.info("FeatureClient2 start !");
        new Thread(nettyClient).start();

    }


    @PreDestroy
    public void stop() {
        log.info("FeatureClient2 stop !");
        nettyClient.stop();
    }


}
