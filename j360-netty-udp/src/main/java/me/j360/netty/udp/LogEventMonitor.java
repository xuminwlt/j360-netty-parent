package me.j360.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import me.j360.netty.udp.codec.LogEventDecode;
import me.j360.netty.udp.handler.LogEventHandler;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午4:59
 * 说明：
 */
public class LogEventMonitor {

    private final NioEventLoopGroup group;
    private final Bootstrap bootstrap;
    //private final InetSocketAddress address;

    public LogEventMonitor(InetSocketAddress address) {

        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new LogEventDecode())
                                .addLast(new LogEventHandler());
                    }
                }).localAddress(address);
    }

    public Channel bind() {
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }

}
