package com.xy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author margln
 * @date 2024/4/17
 */
@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = "com.xy.*.mapper")
public class FeatureApp {


    public static void main(String[] args) {
        SpringApplication.run(FeatureApp.class, args);
    }
}
