package me.j360.netty.agent;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import me.j360.netty.agent.handler.AgentServerHandler;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午1:42
 * 说明：
 */
public class BootstrapApplication {

    public static void main(String[] args) {

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .remoteAddress(new InetSocketAddress("localhost",8888))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new IdleStateHandler(10, 10, 30));
                        //socketChannel.pipeline().addLast(echoClientHandler);
                    }
                });

        //need to be manage
        final AgentServerHandler agentServerHandler = new AgentServerHandler(bootstrap);

        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(8000))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new IdleStateHandler(10, 10, 30));
                        socketChannel.pipeline().addLast(agentServerHandler);
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
