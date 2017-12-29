package me.j360.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.j360.netty.server.handler.EchoServerHandler;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/25 下午4:57
 * 说明：
 */
public class BootstrapApplication {


    public static void main(String[] args) {

        final EchoServerHandler echoServerHandler = new EchoServerHandler();

        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(8888))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(echoServerHandler);
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
