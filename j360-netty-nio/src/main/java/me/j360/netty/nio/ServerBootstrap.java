package me.j360.netty.nio;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author: min_xu
 * @date: 2018/10/25 下午3:24
 * 说明：
 */
public class ServerBootstrap {

    private NioSelectorRunnablePool selectorRunnablePool;

    public ServerBootstrap(NioSelectorRunnablePool pool) {
        this.selectorRunnablePool = pool;
    }

    public void bind(final SocketAddress localAddress) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(localAddress);

            Boss nextBoss = selectorRunnablePool.nextBoss();
            nextBoss.registerAcceptChannelTask(serverSocketChannel);

        } catch (Exception e) {

        }
    }
}
