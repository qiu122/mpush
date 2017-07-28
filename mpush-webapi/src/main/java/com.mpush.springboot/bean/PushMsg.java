package com.mpush.springboot.bean;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by qiu.xiaolong on 2017/7/28.
 */
@Entity
public class PushMsg {

    @Id
    private ObjectId id;
    /**
     * 发送人ClientId
     */
    private String clientId;
    /**
     * 发送主题
     */
    private String[] topics;

    /**
     * 发送时间
     */
    private String pushTime;

    /**
     * 消息接收人
     */
    private String[] toClients;

    /**
     * 消息有效时间，单位：分
     */
    private Integer expiredMinute;

    /**
     * 即时送达，过期不侯
     */
    private Boolean expirededImmediately;

    /**
     * 消息内容
     */
    private String message;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String[] getToClients() {
        return toClients;
    }

    public void setToClients(String[] toClients) {
        this.toClients = toClients;
    }

    public Integer getExpiredMinute() {
        return expiredMinute;
    }

    public void setExpiredMinute(Integer expiredMinute) {
        this.expiredMinute = expiredMinute;
    }

    public Boolean getExpirededImmediately() {
        return expirededImmediately;
    }

    public void setExpirededImmediately(Boolean expirededImmediately) {
        this.expirededImmediately = expirededImmediately;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
