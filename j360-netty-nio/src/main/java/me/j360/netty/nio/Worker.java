package me.j360.netty.nio;

import java.nio.channels.SocketChannel;

/**
 * @author: min_xu
 * @date: 2018/10/25 上午11:40
 * 说明：
 */
public interface Worker {

    void registerNewChannelTask(SocketChannel socketChannel);
}
