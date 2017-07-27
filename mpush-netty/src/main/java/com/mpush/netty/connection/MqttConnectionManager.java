package com.mpush.netty.connection;

import com.mpush.api.connection.Connection;
import com.mpush.api.connection.ConnectionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by qiu.xiaolong on 2017/7/27.
 */
public class MqttConnectionManager implements ConnectionManager {
    private final ConcurrentMap<ChannelId, Connection> connections = new ConcurrentHashMap<>();

    @Override
    public Connection get(Channel channel) {
        return connections.get(channel.id());
    }

    @Override
    public Connection removeAndClose(Channel channel) {
        return connections.remove(channel.id());
    }

    @Override
    public void add(Connection connection) {
        connections.putIfAbsent(connection.getChannel().id(), connection);
    }

    @Override
    public int getConnNum() {
        return connections.size();
    }

    public ConcurrentMap<ChannelId, Connection> getAllConnections(){
        return this.connections;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        connections.values().forEach(Connection::close);
        connections.clear();
    }
}
