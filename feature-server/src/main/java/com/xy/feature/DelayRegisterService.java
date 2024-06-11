package com.xy.feature;

import com.xy.data.domain.User;
import com.xy.data.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClientImportSelector;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author margln
 * @date 2024/3/15
 */
@Slf4j
@Service
@Order(value = 10000)
public class DelayRegisterService {

    @Resource
    UserService userService;

    @Value("${spring.application.name}")
    String appName;

    private List<User> userList;

    @PostConstruct
    public void delayRegister(){

        log.debug("-------- init data before register eureka server start ------");
        initUserData();
        log.debug("-------- init data before register eureka server end ------");

        log.debug("-------- after init data register eureka server start ------");
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.register(EnableDiscoveryClientImportSelector.class);
//        context.refresh();
        log.debug("-------- after init data register eureka server done ------");

    }

    public List<User> getUserList() {
        return userList;
    }

    private void initUserData() {
        this.userList = userService.list();
    }
}
