package com.xy.gateway;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author margln
 * @date 2024/3/16
 */
@SpringBootApplication
@EnableEurekaClient
//@EnableApolloConfig(value = "application.yml")
public class GatewayApp {

    public static void main(String[] args) {

        //GatewayProperties
        SpringApplication.run(GatewayApp.class, args);
    }
}
