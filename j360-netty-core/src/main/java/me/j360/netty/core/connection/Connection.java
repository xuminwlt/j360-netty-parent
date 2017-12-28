package me.j360.netty.core.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import me.j360.netty.core.protocol.Packet;

/**
 * @author: min_xu
 * @date: 2017/12/27 下午5:00
 * 说明：
 */
public interface Connection {


    byte STATUS_NEW = 0;
    byte STATUS_CONNECTION = 1;
    byte STATUS_DISCONNECTION = 2;

    void init(Channel channel, boolean security);

    ChannelFuture send(Packet packet);

    ChannelFuture send(Packet packet, ChannelFutureListener listener);

    SessionContext getSessionContext();

    void setSessionContext(SessionContext sessionContext);

    String getId();

    ChannelFuture close();

    boolean isConnected();

    boolean isReadTimeout();

    boolean isWriteTimeout();

    void updateLastReadTime();

    void updateLastWriteTime();

    Channel getChannel();



}
