package me.j360.netty.nio;


import me.j360.netty.nio.client.TimeClientHandle;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @author: min_xu
 * @date: 2018/10/25 上午11:39
 * 说明：
 */
public class Main {

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                NioSelectorRunnablePool nioSelectorRunnablePool = new NioSelectorRunnablePool(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

                ServerBootstrap bootstrap = new ServerBootstrap(nioSelectorRunnablePool);
                bootstrap.bind(new InetSocketAddress(1234));

                System.out.println("start Server");
            }
        }).start();

        int port = 1234;
        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
    }
}
