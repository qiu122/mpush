package com.mpush.springboot.properties;

/**
 * Created by qiu.xiaolong on 2017/6/15.
 */
public class RocketMq {
    private String nameServer;
    private String topic;
    private String tags;
    private String key;

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
