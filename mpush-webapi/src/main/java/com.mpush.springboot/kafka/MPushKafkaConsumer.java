package com.mpush.springboot.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiu.xiaolong on 2017/7/31.
 */
public class MPushKafkaConsumer implements Runnable{
    static volatile   AtomicInteger threadCount = new AtomicInteger(1);
    public static void main(String[] args) {

        for(int i=0;i<2;i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Properties props = new Properties();
                        props.put("bootstrap.servers", "127.0.0.1:9092");
                        String groupName = "test "+threadCount.getAndIncrement();
                        props.put("group.id", groupName);
                        props.put("enable.auto.commit", "true");
                        props.put("auto.commit.interval.ms", "1000");
                        props.put("session.timeout.ms", "30000");
                        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
                        consumer.subscribe(Arrays.asList("topic1"));
                        while (true) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ConsumerRecords<String, String> records = consumer.poll(100);
                            for (ConsumerRecord<String, String> record : records)
                                System.out.printf("group name = %s,offset = %d, key = %s, value = %s\n",Thread.currentThread().getName(), record.offset(), record.key(), record.value());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    @Override
    public void run() {
//        while (true) {
            try {
                Thread.sleep(200);
                synchronized (this){
                    System.out.println(Thread.currentThread().getName()+"："+threadCount.getAndAdd(1));

                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        }
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "127.0.0.1:9092");
//        String groupName = "test "+threadCount.getAndIncrement();
//        props.put("group.id", groupName);
//        props.put("enable.auto.commit", "true");
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("session.timeout.ms", "30000");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//        consumer.subscribe(Arrays.asList("topic1"));
//        while (true) {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ConsumerRecords<String, String> records = consumer.poll(100);
//            for (ConsumerRecord<String, String> record : records)
//                System.out.printf("group name name = %s,offset = %d, key = %s, value = %s\n",groupName, record.offset(), record.key(), record.value());
//        }
    }
}
