package me.j360.netty.core.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import me.j360.netty.core.api.Listener;
import me.j360.netty.core.api.Server;
import me.j360.netty.core.constants.ThreadNames;
import me.j360.netty.core.exception.NettyServiceException;
import me.j360.netty.core.tcp.BaseService;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午8:00
 * 说明：
 */

public abstract class NettyUDPConnector extends BaseService implements Server {

    private final int port;
    private EventLoopGroup eventLoopGroup;

    public NettyUDPConnector(int port) {
        this.port = port;
    }

    @Override
    protected void doStart(Listener listener)throws Throwable {
        createNioServer(listener);
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        eventLoopGroup.shutdownGracefully().syncUninterruptibly();
    }

    private void createServer(Listener listener, EventLoopGroup eventLoopGroup, ChannelFactory<? extends Channel> channelFactory) {
        this.eventLoopGroup = eventLoopGroup;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channelFactory(channelFactory)
                    .option(ChannelOption.SO_BROADCAST, Boolean.TRUE)
                    .handler(getChannelHandler());

            initOptions(bootstrap);

            bootstrap.bind(port).addListener(future -> {

            });
        } catch (Exception e) {
            //("udp server start exception", e);
            if (listener != null) listener.onFailure(e);
            throw new NettyServiceException("udp server start exception, port=" + port, e);
        }
    }

    private void createNioServer(Listener listener) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory(ThreadNames.T_GATEWAY_WORKER));
        eventLoopGroup.setIoRatio(100);
        createServer(listener, eventLoopGroup, () -> new NioDatagramChannel(InternetProtocolFamily.IPv4));
    }

    private void createEpollServer(Listener listener) {
        EpollEventLoopGroup eventLoopGroup = new EpollEventLoopGroup(1, new DefaultThreadFactory(ThreadNames.T_GATEWAY_WORKER));
        eventLoopGroup.setIoRatio(100);
        createServer(listener, eventLoopGroup, () -> new NioDatagramChannel(InternetProtocolFamily.IPv4));
    }

    protected void initOptions(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
    }


    public abstract ChannelHandler getChannelHandler();
}
