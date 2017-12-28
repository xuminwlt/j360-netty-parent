package me.j360.netty.core.client;

import com.sun.deploy.config.ClientConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.j360.netty.core.api.Listener;
import me.j360.netty.core.codec.PacketDecode;
import me.j360.netty.core.codec.PacketEncoder;
import me.j360.netty.core.handler.ConnClientChannelHandler;
import me.j360.netty.core.tcp.BaseService;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午6:35
 * 说明：
 */
public final class DeviceClient extends BaseService {

    private Bootstrap bootstrap;
    private NioEventLoopGroup workerGroup;


    @Override
    protected void doStart(Listener listener) throws Throwable {

        this.workerGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)//
                .option(ChannelOption.TCP_NODELAY, true)//
                .option(ChannelOption.SO_REUSEADDR, true)//
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60 * 1000)
                .option(ChannelOption.SO_RCVBUF, 5 * 1024 * 1024)
                .channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() { // (4)
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new PacketDecode());
                ch.pipeline().addLast("encoder", PacketEncoder.INSTANCE);
                ch.pipeline().addLast("handler", new ConnClientChannelHandler());
            }
        });

        listener.onSuccess();
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        if (workerGroup != null) workerGroup.shutdownGracefully();

        listener.onSuccess();
    }



    public ChannelFuture connect(InetSocketAddress remote, InetSocketAddress local, ClientConfig clientConfig) {
        ChannelFuture future = local != null ? bootstrap.connect(remote, local) : bootstrap.connect(remote);
        /*if (future.channel() != null) future.channel().attr(CONFIG_KEY).set(clientConfig);
        future.addListener(f -> {
            if (f.isSuccess()) {
                future.channel().attr(CONFIG_KEY).set(clientConfig);
                LOGGER.info("start netty client success, remote={}, local={}", remote, local);
            } else {
                LOGGER.error("start netty client failure, remote={}, local={}", remote, local, f.cause());
            }
        });*/
        return future;
    }

    public ChannelFuture connect(String host, int port, ClientConfig clientConfig) {
        return connect(new InetSocketAddress(host, port), null, clientConfig);
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public NioEventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

}
