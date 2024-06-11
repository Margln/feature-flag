package com.xy.config;

import com.netflix.discovery.DiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClientImportSelector;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author margln
 * @date 2024/3/15
 */
@Slf4j
@Service
@Order(value = 10000)
public class DelayRegisterService {

    @Value("${spring.application.name}")
    String appName;

    DiscoveryClient discoveryClient;

    @PostConstruct
    public void delayRegister(){

        log.debug("-------- init data before register eureka server start ------");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        log.debug("-------- init data before register eureka server end ------");

        log.debug("-------- after init data register eureka server start ------");
      /*  AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EnableDiscoveryClientImportSelector.class);
        context.refresh();*/

       // EurekaDiscoveryClientConfiguration




        log.debug("-------- after init data register eureka server done ------");

    }

}
