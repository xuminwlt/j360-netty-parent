package me.j360.netty.core.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.Native;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import me.j360.netty.core.api.Client;
import me.j360.netty.core.api.Listener;
import me.j360.netty.core.codec.PacketDecode;
import me.j360.netty.core.codec.PacketEncoder;
import me.j360.netty.core.constants.ThreadNames;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午8:01
 * 说明：
 */
public abstract class NettyTCPClient extends BaseService implements Client {

    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;


    @Override
    protected void doStart(Listener listener) throws Throwable {
        if (userNettyEpoll()) {
            createEpollClient(listener);
        } else {
            createNioClient(listener);
        }
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {

    }

    protected void initPipeline(ChannelPipeline pipeline) {
        //pipeline.addLast("decoder", getDecoder());
        //pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("handler", getChannelHandler());
    }

    public ChannelFuture connect(String host, int port) {
        return bootstrap.connect(new InetSocketAddress(host, port));
    }

    public ChannelFuture connect(String host, int port, Listener listener) {
        return bootstrap.connect(new InetSocketAddress(host, port)).addListener(f -> {
            if (f.isSuccess()) {
                listener.onSuccess();
            }
        });
    }

    protected abstract ChannelHandler getChannelHandler();

    private void createClient(Listener listener, EventLoopGroup workerGroup, ChannelFactory<? extends Channel> channelFactory) {

        this.workerGroup = workerGroup;
        this.bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channelFactory(channelFactory);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                initPipline(ch.pipeline());
            }
        });

        initOptions(bootstrap);
        listener.onSuccess();
    }


    private void createNioClient(Listener listener) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getWorkThreadNum(), new DefaultThreadFactory(ThreadNames.T_TCP_CLIENT), getSelectorProvider());
        nioEventLoopGroup.setIoRatio(getIoRate());
        createClient(listener, nioEventLoopGroup, getChannelFactory());
    }

    private void createEpollClient(Listener listener) {
        EpollEventLoopGroup nioEventLoopGroup = new EpollEventLoopGroup(getWorkThreadNum(), new DefaultThreadFactory(ThreadNames.T_TCP_CLIENT));
        nioEventLoopGroup.setIoRatio(getIoRate());
        createClient(listener, nioEventLoopGroup, getChannelFactory());
    }

    protected void initPipline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("handler", getChannelHandler());
    }

    protected void initOptions(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 4000);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    protected ChannelHandler getDecoder() {
        return new PacketDecode();
    }

    protected ChannelHandler getEncoder() {
        return PacketEncoder.INSTANCE;
    }

    protected int getIoRate() {
        return 50;
    }

    protected int getWorkThreadNum() {
        return 1;
    }

    private boolean userNettyEpoll() {
        if (false) {
            try {
                Native.offsetofEpollData();
                return true;
            } catch (UnsatisfiedLinkError error) {

            }
        }
        return false;
    }


    public ChannelFactory<? extends Channel> getChannelFactory() {
        return NioSocketChannel::new;
    }

    public SelectorProvider getSelectorProvider() {
        return SelectorProvider.provider();
    }



}
