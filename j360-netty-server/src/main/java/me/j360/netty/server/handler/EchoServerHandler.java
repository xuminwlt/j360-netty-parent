package me.j360.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author: min_xu
 * @date: 2017/12/25 下午5:41
 * 说明：
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {

        System.out.println("server ready");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("receive -> "+byteBuf.toString(Charset.forName("UTF-8")));

        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("hello client, I am server", CharsetUtil.UTF_8));
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        System.out.println("channelInactive");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

        System.out.println("userEventTriggered" + evt.toString());
    }

}
