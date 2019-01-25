package me.j360.dubbo;

import io.netty.channel.ChannelHandler;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author: min_xu
 * @date: 2018/11/21 11:45 AM
 * 说明：
 */
public interface Server {

    /**
     * get channel handler.
     *
     * @return channel handler
     */
    ChannelHandler getChannelHandler();

    /**
     * get local address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * send message.
     *
     * @param message
     * @throws RuntimeException
     */
    void send(Object message) throws RuntimeException;

    /**
     * send message.
     *
     * @param message
     * @param sent    already sent to socket?
     */
    void send(Object message, boolean sent) throws RuntimeException;

    /**
     * close the channel.
     */
    void close();

    /**
     * Graceful close the channel.
     */
    void close(int timeout);

    void startClose();

    /**
     * is closed.
     *
     * @return closed
     */
    boolean isClosed();


    /**
     * is bound.
     *
     * @return bound
     */
    boolean isBound();

    /**
     * get channels.
     *
     * @return channels
     */
    Collection<NettyChannel> getChannels();

    /**
     * get channel.
     *
     * @param remoteAddress
     * @return channel
     */
    NettyChannel getChannel(InetSocketAddress remoteAddress);

}
