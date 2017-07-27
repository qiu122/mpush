package com.mpush.core.mqtt.impl;

import com.mpush.api.connection.Connection;
import com.mpush.api.connection.ConnectionManager;
import com.mpush.core.mqtt.MqttService;
import com.mpush.core.session.ReusableSession;
import com.mpush.core.session.ReusableSessionManager;
import com.mpush.netty.connection.MqttConnectionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.mqtt.*;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiu.xiaolong on 2017/7/25.
 */
public class MqttServiceImpl implements MqttService{
    private final MqttConnectionManager connectionManager;

    public MqttServiceImpl(MqttConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    @Override
    public void connect(String clientId, String sessionId, String userId) {
//        ReusableSession session = ReusableSessionManager.I.querySession(sessionId);
    }

    @Override
    public void publish(long userId, String msg) {

    }

    @Override
    public void disconnect(long userId) {

    }

    @Override
    public void pushMessageToClients(String message, String topicName, AtomicInteger nextMessageId ) {
        try {
            final byte[] messageData = message.getBytes();
            final MqttPublishVariableHeader vheader = new MqttPublishVariableHeader(topicName, nextMessageId.incrementAndGet());
            final MqttFixedHeader fixedHeader = createFixedHeader(MqttMessageType.PUBLISH, topicName.length() + 4 + messageData.length);
            ConcurrentMap<ChannelId, Connection> allConnections = connectionManager.getAllConnections();
            for(ChannelId conn:allConnections.keySet()) {
                Connection connection = allConnections.get(conn);
                final ByteBuf payload = Unpooled.wrappedBuffer(messageData);
                final MqttMessage pmessage = MqttMessageFactory.newMessage(fixedHeader, vheader, payload);
                connection.getChannel().writeAndFlush(pmessage);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    private MqttFixedHeader createFixedHeader(MqttMessageType mtype, int remainLength) {
        return new MqttFixedHeader(mtype, false, MqttQoS.AT_LEAST_ONCE, false, remainLength);
    }
}
