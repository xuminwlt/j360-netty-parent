package me.j360.netty.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.j360.netty.core.protocol.Packet;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午7:47
 * 说明：
 */

@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    public static final PacketEncoder INSTANCE = new PacketEncoder();

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {

    }


}
