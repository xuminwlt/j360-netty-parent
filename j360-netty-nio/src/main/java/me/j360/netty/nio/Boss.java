package me.j360.netty.nio;

import java.nio.channels.ServerSocketChannel;

/**
 * @author: min_xu
 * @date: 2018/10/25 上午11:40
 * 说明：
 */
public interface Boss {

    void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel);
}
