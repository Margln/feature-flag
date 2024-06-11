package com.xy.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author margln
 * @date 2024/3/15
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApp {


    public static void main(String[] args) {
        SpringApplication.run(EurekaApp.class, args);
    }
}
