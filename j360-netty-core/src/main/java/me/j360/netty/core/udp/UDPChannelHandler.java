package me.j360.netty.core.udp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.j360.netty.core.connection.NettyConnection;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author: min_xu
 * @date: 2017/12/27 下午4:51
 * 说明：
 */

@ChannelHandler.Sharable
public final class UDPChannelHandler extends ChannelInboundHandlerAdapter {


    private final NettyConnection connection = new NettyConnection();
    //private final JdpPacketReader
    private InetAddress multicastAddress;
    private NetworkInterface networkInterface;

    public UDPChannelHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

}
