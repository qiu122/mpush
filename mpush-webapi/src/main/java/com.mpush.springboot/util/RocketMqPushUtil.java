package com.mpush.springboot.util;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.mpush.springboot.properties.UserDefinedProperty;


public class RocketMqPushUtil {

    private String nameServer;
    private String topic;
    private String tags;
    private static RocketMqPushUtil instance = null;
    private static DefaultMQProducer producer;

    public static RocketMqPushUtil getInstance(UserDefinedProperty userDefinedProperty) {
        if(instance == null){
            instance = new RocketMqPushUtil();
            instance.setNameServer(userDefinedProperty.getRocketMq().getNameServer());
            instance.setTopic(userDefinedProperty.getRocketMq().getTopic());
            instance.setTags(userDefinedProperty.getRocketMq().getTags());

            producer = new DefaultMQProducer("ProducerGroupName");
            producer.setNamesrvAddr(instance.getNameServer());
            producer.setInstanceName("RocketProducer");
            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
             * 注意：切记不可以在每次发送消息时，都调用start方法
             */
            try {
                producer.start();
            } catch (MQClientException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public int push(String message,String key) {

        /**
         * 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。
         * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态，<br>
         * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，<br>
         * 需要对这种情况做处理。另外，消息可能会存在发送失败的情况，失败重试由应用来处理。
         */
        try {
            byte[] bytes = message.getBytes();
            Message msg = new Message(topic,// topic
                    tags,// tag
                    key,// key
                    bytes);// body
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void shutdown(){
        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        producer.shutdown();
    }

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

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("需要： 参数配置文件<stdin.xml>所在的hdfs目录");
            System.exit(-1);
        }
    }


}