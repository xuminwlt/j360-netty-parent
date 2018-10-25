package me.j360.netty.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author: min_xu
 * @date: 2018/10/25 下午2:27
 * 说明：
 */
public class NioServerWorker extends AbstractNioSelector implements Worker {

    NioServerWorker(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
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
        Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            SocketChannel channel = (SocketChannel) key.channel();

            int ret = 0;
            boolean fail = true;
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            try {
                ret = channel.read(byteBuffer);
                fail = false;
            } catch (Exception e) {

            }
            if (ret<= 0 || fail) {
                key.cancel();
                System.out.println("客户端断开");
            } else {
                System.out.println("收到数据");

                ByteBuffer outBuffer = ByteBuffer.wrap("收到".getBytes("UTF-8"));
                channel.write(outBuffer);
            }
        }
    }

    @Override
    public void registerNewChannelTask(SocketChannel socketChannel) {
        final Selector selector = this.selector;
        registerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
