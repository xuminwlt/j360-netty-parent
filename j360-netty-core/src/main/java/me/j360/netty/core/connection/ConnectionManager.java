package me.j360.netty.core.connection;

import io.netty.channel.Channel;

/**
 * @author: min_xu
 * @date: 2017/12/27 下午5:17
 * 说明：
 */
public interface ConnectionManager {

    Connection get(Channel channel);

    Connection removeAndClose(Channel channel);

    void add(Connection connection);

    int getConnNum();

    void init();

    void destory();
}
