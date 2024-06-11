package com.xy.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.data.domain.User;
import com.xy.data.service.UserService;
import com.xy.data.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    @Resource
    private ApplicationEventPublisher publisher;

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public User getData() {
        User byId = getById(1);
        ageIncr(byId.getAge());
        return byId;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void ageIncr(Byte age) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, 1)
                .setSql("age = age + 1");
        int rows = baseMapper.update(null, updateWrapper);
        publisher.publishEvent("this is a transactional commit event! ");
        if (age > 17) {
            throw new RuntimeException(" unknown exp ... ");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(Object event) {
        log.info("TransactionalEventListener 事务提交后执行, event -> {}", event.toString());
    }
}




