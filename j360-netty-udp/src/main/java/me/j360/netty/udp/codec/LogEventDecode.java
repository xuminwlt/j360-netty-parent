package me.j360.netty.udp.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import me.j360.netty.udp.domain.LogEvent;

import java.util.List;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午4:46
 * 说明：
 */
public class LogEventDecode extends MessageToMessageEncoder<DatagramPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf data = msg.content();
        int idx = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        String filename =  data.slice(0, idx).toString(CharsetUtil.UTF_8);
        String logMsg = data.slice(idx+1, data.readableBytes()).toString(CharsetUtil.UTF_8);

        LogEvent logEvent = new LogEvent(msg.sender(), System.currentTimeMillis(), filename, logMsg);
        out.add(logEvent);
    }
}
