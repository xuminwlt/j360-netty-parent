package me.j360.netty.nio;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author: min_xu
 * @date: 2018/10/25 下午2:09
 * 说明：
 */
public class NioServerBoss extends AbstractNioSelector implements Boss {

    NioServerBoss(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        super(executor, threadName, selectorRunnablePool);
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select();
    }

    @Override
    protected void process(Selector selector) throws IOException {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()) {
            return;
        }
        for (Iterator<SelectionKey> it = selectionKeys.iterator(); it.hasNext(); ) {
            SelectionKey key = it.next();
            it.remove();

            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            Worker nextWorker = getSelectorRunnablePool().nextWorker();
            nextWorker.registerNewChannelTask(socketChannel);

            System.out.println("客户端连接");
        }
    }

    @Override
    public void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel) {
        final Selector selector = this.selector;
        registerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
