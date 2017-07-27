package com.mpush.core.server;

import com.mpush.api.connection.ConnectionManager;
import com.mpush.core.handler.AdminHandler;
import com.mpush.netty.connection.MqttConnectionManager;
import com.mpush.netty.server.NettyMqttServer;
import com.mpush.tools.config.CC;
import com.mpush.tools.thread.ThreadNames;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by qiu.xiaolong on 2017/7/20.
 */
public class DeviceMqttServer  extends NettyMqttServer {
    private static DeviceMqttServer I;

    private DeviceMqttHandler deviceMqttHandler;

    private MqttConnectionManager connectionManager = new MqttConnectionManager();

    public static DeviceMqttServer I() {
        if (I == null) {
            synchronized (DeviceMqttServer.class) {
                I = new DeviceMqttServer();
            }
        }
        return I;
    }

    private DeviceMqttServer() {
        super(CC.mp.net.mqtt_server_port);
    }

    @Override
    public void init() {
        super.init();
        this.deviceMqttHandler = new DeviceMqttHandler(connectionManager);
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
//        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        super.initPipeline(pipeline);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return deviceMqttHandler;
    }

    @Override
    protected ChannelHandler getDecoder() {
        return new StringDecoder();
    }

    @Override
    protected ChannelHandler getEncoder() {
        return new StringEncoder();
    }

    @Override
    protected int getWorkThreadNum() {
        return 1;
    }

    @Override
    protected String getBossThreadName() {
        return ThreadNames.T_MQTT_BOSS;
    }

    @Override
    protected String getWorkThreadName() {
        return ThreadNames.T_MQTT_WORKER;
    }
}