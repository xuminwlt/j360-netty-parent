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
 * @date: 2017/12/28 下午5:19
 * 说明：
 */

@Slf4j
@ChannelHandler.Sharable
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    private final ConnectionManager connectionManager;


    public ServerChannelHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = (Packet) msg;
        byte cmd = packet.cmd;

        try {
            //Profiler.start("time cost on [channel read]: ", packet.toString());
            Connection connection = connectionManager.get(ctx.channel());
            log.debug("channelRead conn={}, packet={}", ctx.channel(), connection.getSessionContext(), msg);
            connection.updateLastReadTime();
            //receiver.onReceive(packet, connection);
        } finally {

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("client connected conn={}", ctx.channel());
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Connection connection = connectionManager.removeAndClose(ctx.channel());
        log.debug("client disconnected conn={}", connection);
    }
}
