package com.mpush.springboot.service.impl;

import com.mpush.common.message.PushMessage;
import com.mpush.springboot.service.PushService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by qiu.xiaolong on 2017/7/28.
 */
@Service
public class PushServiceImpl implements PushService{

//    @Autowired
//    private RedisTemplate redisTemplate;

    public boolean pushMessage(PushMessage message){

        return  true;
    }
}
