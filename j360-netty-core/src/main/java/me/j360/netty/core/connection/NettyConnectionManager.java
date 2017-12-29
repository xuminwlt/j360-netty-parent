package me.j360.netty.core.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: min_xu
 * @date: 2017/12/27 下午4:53
 * 说明：
 */
public class NettyConnectionManager implements ConnectionManager {

    private final ConcurrentMap<ChannelId, Connection> connections = new ConcurrentHashMap<ChannelId, Connection>();

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

    @Override
    public void init() {

    }



    @Override
    public void destory() {
        connections.values().forEach(Connection :: close);
        connections.clear();
    }

    @Override
    public Connection getFirst() {
        return connections.values().stream().findAny().get();
    }

}
