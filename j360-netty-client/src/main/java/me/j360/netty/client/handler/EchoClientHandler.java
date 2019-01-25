package me.j360.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.concurrent.*;

/**
 * @author: min_xu
 * @date: 2017/12/25 下午5:49
 * 说明：
 */

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello proxy, I am client", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        System.out.println("receive:" + byteBuf.toString(Charset.forName("UTF-8")));
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello proxy, ping", CharsetUtil.UTF_8));
            }
        }, 1, 3, TimeUnit.SECONDS);
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
