package me.j360.netty.core.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import me.j360.netty.core.connection.Connection;
import me.j360.netty.core.connection.ConnectionManager;
import me.j360.netty.core.connection.NettyConnection;
import me.j360.netty.core.protocol.Packet;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午5:17
 * 说明：
 */

@Slf4j
@ChannelHandler.Sharable
public class GatewayClientChannelHandler extends ChannelInboundHandlerAdapter {

    private final ConnectionManager connectionManager;


    public GatewayClientChannelHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        //this.receiver = receiver;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("receive gateway packet={}, channel={}", msg, ctx.channel());
        Packet packet = (Packet) msg;
        //receiver.onReceive(packet, connectionManager.get(ctx.channel()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());

        log.error("caught an ex, channel={}, conn={}", ctx.channel(), connection, cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("client connected conn={}", ctx.channel());
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = connectionManager.removeAndClose(ctx.channel());
        log.info("client disconnected conn={}", connection);
    }

}
