package com.mpush.netty.client;

import org.fusesource.mqtt.client.*;

import java.sql.Date;
import java.util.UUID;

import static org.fusesource.hawtbuf.UTF8Buffer.utf8;

/**
 * Created by qiu.xiaolong on 2017/7/21.
 */
public class MqttClient {

    public static void main(String[] args) throws Exception {

        String clientId = UUID.randomUUID().toString().replaceAll("-","").substring(0,23);
        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);
        mqtt.setClientId(clientId);
        FutureConnection connection = mqtt.futureConnection();
        Future<Void> f1 = connection.connect();
        f1.await();

        Future<byte[]> f2 = connection.subscribe(new Topic[]{new Topic(utf8("foo"), QoS.AT_LEAST_ONCE)});
        byte[] qoses = f2.await();

        // We can start future receive..
        Future<Message> receive = connection.receive();

        // send the message..
        Future<Void> f3 = connection.publish("foo", "Hello".getBytes(), QoS.AT_LEAST_ONCE, false);
        f3.await();
        f3.then(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println(aVoid);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
        // Then the receive will get the message.
        Message message = receive.await();
        System.out.println(new String(message.getPayload()));
        message.ack();

        Future<Void> f4 = connection.disconnect();
        f4.await();
    }
}
