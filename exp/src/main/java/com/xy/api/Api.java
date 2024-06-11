package com.xy.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author margln
 * @date 2024/3/15
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class Api {

    @Resource
    ApiFeignClient apiFeignClient;

    @Value("${spring.application.name}")
    String appName;

    @Value("${server.port}")
    Integer port;

    @Value("${eureka.instance.metadata-map.envTag}")
    String envTag;


    @RequestMapping("/getinstance")
    public Object getInstance() {
        log.info("getInstance appName:{}, port:{}, envTag:{}", appName, port, envTag);
        Map<String, Object> map = new HashMap<>();
        return String.format(" getInstance appName:%s, port:%d, envTag:%s ", appName, port, envTag);

    }

    @RequestMapping("/getClient")
    public Object getClient () {
        try {
            return apiFeignClient.getInstance();
        }catch (Exception e) {
            log.error("{}, ", e.getMessage(), e);
        }
        return "error";

    }




}
