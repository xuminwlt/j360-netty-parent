package me.j360.netty.udp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.j360.netty.udp.domain.LogEvent;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午4:51
 * 说明：
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {

        System.out.println(msg.toString());

    }
}
