package me.j360.netty.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import me.j360.netty.core.connection.Connection;
import me.j360.netty.core.connection.ConnectionManager;
import me.j360.netty.core.connection.NettyConnection;

import java.nio.charset.Charset;

/**
 * @author: min_xu
 * @date: 2017/12/25 下午5:41
 * 说明：
 */

@ChannelHandler.Sharable
@Slf4j
public final class EchoServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final ConnectionManager connectionManager;


    public EchoServerHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        log.debug("client connected conn={}", ctx.channel());
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
        System.out.println("server ready");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        System.out.println(String.format("channelRead conn=%s, packet=%s", ctx.channel(), connection.getSessionContext(), byteBuf.toString(Charset.forName("UTF-8"))));
        connection.updateLastReadTime();

        System.out.println("receive -> "+byteBuf.toString(Charset.forName("UTF-8")));
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client, I am server", CharsetUtil.UTF_8));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Connection connection = connectionManager.removeAndClose(ctx.channel());
        log.debug("client disconnected conn={}", connection);
    }

}
