package me.j360.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.j360.netty.client.handler.EchoClientHandler;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/25 下午4:57
 * 说明：
 */
public class BootstrapApplication {


    public static void main(String[] args) {
        final EchoClientHandler echoClientHandler = new EchoClientHandler();

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .remoteAddress(new InetSocketAddress("localhost",8888))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(echoClientHandler);
                    }
                });

        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
            nioEventLoopGroup.shutdownGracefully();
        }

    }
}
