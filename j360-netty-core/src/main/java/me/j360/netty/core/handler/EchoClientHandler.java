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
 * @date: 2017/12/25 下午5:49
 * 说明：
 */
@ChannelHandler.Sharable
@Slf4j
public final class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final ConnectionManager connectionManager;

    public EchoClientHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        //this.receiver = receiver;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);

        System.out.println("client ready");
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello server, I am client", CharsetUtil.UTF_8));
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

        System.out.println("receive:" + byteBuf.toString(Charset.forName("UTF-8")));

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = connectionManager.removeAndClose(ctx.channel());
        log.info("client disconnected conn={}", connection);
    }
}
