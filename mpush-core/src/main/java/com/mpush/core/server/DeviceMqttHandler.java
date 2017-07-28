package com.mpush.core.server;

import com.mpush.api.connection.Connection;
import com.mpush.api.connection.ConnectionManager;
import com.mpush.api.event.ConnectionCloseEvent;
import com.mpush.core.mqtt.MqttService;
import com.mpush.core.mqtt.impl.MqttServiceImpl;
import com.mpush.netty.connection.MqttConnectionManager;
import com.mpush.netty.connection.NettyConnection;
import com.mpush.tools.event.EventBus;
import com.mpush.tools.log.Logs;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by qiu.xiaolong on 2017/7/20.
 */
@ChannelHandler.Sharable
public class DeviceMqttHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMqttHandler.class);
    private final MqttConnectionManager connectionManager;

    public DeviceMqttHandler(MqttConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        Logs.CONN.error("client caught ex, conn={}", connection);
//        LOGGER.error("caught an ex, channel={}, conn={}", ctx.channel(), connection, cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logs.CONN.info("client connected conn={}", ctx.channel());
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = connectionManager.removeAndClose(ctx.channel());
        EventBus.I.post(new ConnectionCloseEvent(connection));
        Logs.CONN.info("client disconnected conn={}", connection);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage mqttMessage) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        LOGGER.debug("channelRead conn={}, packet={}", ctx.channel(), connection.getSessionContext());
        MqttFixedHeader fixedHeader = null;
        Object o = mqttMessage.variableHeader();
        switch (mqttMessage.fixedHeader().messageType()){
            case CONNECT:
                MqttConnectMessage connMsg = (MqttConnectMessage)mqttMessage;
                fixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK,false, MqttQoS.AT_MOST_ONCE,false,0);
                MqttConnAckVariableHeader header = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED,false);
                MqttConnAckMessage ackMsg = new MqttConnAckMessage(fixedHeader,header);
                long userId = NumberUtils.toLong(connMsg.payload().userName());
                connection.getSessionContext().setDeviceId(connMsg.payload().clientIdentifier());
                ctx.channel().writeAndFlush(ackMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                break;
            case SUBSCRIBE:
                MqttSubscribeMessage subMsg = (MqttSubscribeMessage)mqttMessage;
                fixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK,false, MqttQoS.AT_MOST_ONCE,false,0);
                MqttSubAckPayload payload = new MqttSubAckPayload();
                MqttSubAckMessage ackSubMsg = new MqttSubAckMessage(fixedHeader,subMsg.variableHeader(),payload);
                //todo 记录客户端订阅主题
                ctx.channel().writeAndFlush(ackSubMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                break;
            case PINGREQ:
                fixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_LEAST_ONCE, false, 0);
                MqttMessage ack = MqttMessageFactory.newMessage(fixedHeader, null, null);
                ctx.channel().writeAndFlush(ack).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                break;
            case PUBLISH:
                MqttPublishMessage pubMsg = (MqttPublishMessage)mqttMessage;
                fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK,false, MqttQoS.AT_LEAST_ONCE,false,4);
                MqttMessageIdVariableHeader mmih = MqttMessageIdVariableHeader.from(pubMsg.variableHeader().packetId());
                MqttMessage ackPubMsg = MqttMessageFactory.newMessage(fixedHeader, mmih, null);
                final ByteBuf pubPayload = pubMsg.payload();
                final byte[] data = new byte[pubPayload.readableBytes()];
                pubPayload.readBytes(data);
                System.out.println(new String(data));
                //todo 记录客户端发布消息
                ctx.channel().writeAndFlush(ackPubMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                MqttService mqttService = new MqttServiceImpl();
                mqttService.pushMessageToClients(new String(data),"foo",new AtomicInteger(1));
                break;
            case DISCONNECT:
                fixedHeader = new MqttFixedHeader(MqttMessageType.DISCONNECT,false, MqttQoS.AT_LEAST_ONCE,false,4);
                MqttMessage disConnMsg = MqttMessageFactory.newMessage(fixedHeader, null, null);
                ctx.channel().writeAndFlush(disConnMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                break;
        }
    }
}
