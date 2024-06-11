package com.xy.data.service;

import com.xy.data.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface UserService extends IService<User> {

    User getData();

    void ageIncr(Byte age);

}
