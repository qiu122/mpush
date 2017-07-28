package com.mpush.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by qiu.xiaolong on 2017/6/15.
 */

@Component
@ConfigurationProperties(prefix = "user")
public class UserDefinedProperty {
    private RocketMq rocketMq;

    private Env env;

    private MongoPro mongodb;

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }

    public MongoPro getMongodb() {
        return mongodb;
    }

    public void setMongodb(MongoPro mongodb) {
        this.mongodb = mongodb;
    }

    public RocketMq getRocketMq() {
        return rocketMq;
    }

    public void setRocketMq(RocketMq rocketMq) {
        this.rocketMq = rocketMq;
    }
}
