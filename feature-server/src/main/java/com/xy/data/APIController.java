package com.xy.data;

import com.oracle.tools.packager.Log;
import com.xy.data.domain.User;
import com.xy.data.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author margln
 * @date 2024/5/8
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class APIController {


    @Resource
    UserService userService;


    @GetMapping("/getAndIncr")
    public Object getAndIncr() {
        User byId = userService.getData();
        log.info("create time: {}", byId.getCreateTime());
        return byId;
    }


    @GetMapping("/ageIncr")
    public Object ageIncr() {
        User byId = userService.getById(1);
        log.info("create time: {}", byId.getCreateTime());
        userService.ageIncr(byId.getAge());
        byId = userService.getById(1);
        return byId;
    }



}
