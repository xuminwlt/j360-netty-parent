package me.j360.netty.test.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午1:59
 * 说明：
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {


    private final int frameLenth;

    public FixedLengthFrameDecoder(int frameLenth) {
        if (frameLenth <= 0) {

        }
        this.frameLenth = frameLenth;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= frameLenth) {
            ByteBuf byteBuf = in.readBytes(frameLenth);
            out.add(byteBuf);
        }
    }
}
