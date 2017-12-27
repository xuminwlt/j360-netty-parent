package me.j360.netty.udp;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午3:00
 * 说明：
 */
public class ServerBootstrap {


    public static void main(String[] args) {
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(8888));

        try {
            Channel channel = monitor.bind();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            monitor.stop();
        }
    }
}
