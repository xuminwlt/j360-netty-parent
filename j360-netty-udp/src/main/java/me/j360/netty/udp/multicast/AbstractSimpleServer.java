package me.j360.netty.udp.multicast;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import static io.netty.channel.socket.InternetProtocolFamily.IPv4;

/**
 * @author: min_xu
 * @date: 2018/11/16 2:29 PM
 * 说明：
 */

@Slf4j
public abstract class AbstractSimpleServer implements SimpleServer {

    protected final int port;
    private EventLoopGroup eventLoopGroup;


    protected AbstractSimpleServer(int port) {
        this.port = port;
    }


    protected void createServer(Listener listener, EventLoopGroup eventLoopGroup, ChannelFactory<? extends DatagramChannel> channelFactory) {
        this.eventLoopGroup = eventLoopGroup;
        try {
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup)//默认是根据机器情况创建Channel,如果机器支持ipv6,则无法使用ipv4的地址加入组播
                    .channelFactory(channelFactory)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(getChannelHandler());

            initOptions(b);

            //直接绑定端口，不要指定host，不然收不到组播消息
            b.bind(port).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("udp server start success on:{}", port);
                    if (listener != null) listener.onSuccess(port);
                } else {
                    log.error("udp server start failure on:{}", port, future.cause());
                    if (listener != null) listener.onFailure(future.cause());
                }
            });
        } catch (Exception e) {
            log.error("udp server start exception", e);
            if (listener != null) listener.onFailure(e);
            //throw new ServiceException("udp server start exception, port=" + port, e);
        }
    }

    protected void createNioServer(Listener listener) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(
                1, new DefaultThreadFactory("NIOTHREAD")
        );
        eventLoopGroup.setIoRatio(100);
        createServer(listener, eventLoopGroup, () -> new NioDatagramChannel(IPv4));//默认是根据机器情况创建Channel,如果机器支持ipv6,则无法使用ipv4的地址加入组播
    }

    protected void initOptions(Bootstrap b) {
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.option(ChannelOption.SO_REUSEADDR, true);
    }

    public abstract ChannelHandler getChannelHandler();
}
