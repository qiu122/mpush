package com.mpush.core.mqtt;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiu.xiaolong on 2017/7/25.
 */
public interface MqttService {
    /**
     * 建立连接
     */
    void connect(String clientId, String sessionId, String userId);

    /**
     * 发布
     * @param msg
     */
    void publish(long userId, String msg);

    /**
     * 删除连接
     */
    void disconnect(long userId);

    void pushMessageToClients(String message, String topicName, AtomicInteger nextMessageId);
}
