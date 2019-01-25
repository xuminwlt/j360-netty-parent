package me.j360.netty.agent.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午1:42
 * 说明：
 */

//@ChannelHandler.Sharable
public class AgentServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private Bootstrap bootstrap;

    public AgentServerHandler(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    private Map<ChannelId, Channel> channelMap = new ConcurrentHashMap<>();

    //channel should be pooled then use get conn, return conn
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        System.out.println("agent ready");
        channel = bootstrap.connect("localhost",8888).channel();
        //System.out.println("agent ready" + channel.id());
        //channelMap.put(channel.id(), channel);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("receive -> "+byteBuf.toString(Charset.forName("UTF-8")));

        //write to server
        //Channel serverChannel = channelMap.get(channelHandlerContext.channel().id());
        channel.writeAndFlush(Unpooled.copiedBuffer("hello server, I am agent", CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("hello client, I am agent", CharsetUtil.UTF_8));
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        System.out.println("agent channelInactive");
        channelMap.remove(ctx.channel().id());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

}
