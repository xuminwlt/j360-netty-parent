package me.j360.netty.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.j360.netty.core.protocol.Packet;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午7:47
 * 说明：
 */
public class PacketEncode extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {

    }
}
