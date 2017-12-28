package me.j360.netty.core.udp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import me.j360.netty.core.connection.Connection;
import me.j360.netty.core.connection.NettyConnection;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Objects;

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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connection.init(ctx.channel(), false);
        if (Objects.nonNull(multicastAddress)) {
            ((DatagramChannel) ctx.channel()).joinGroup(multicastAddress, networkInterface, null).addListener(future -> {
                if (future.isSuccess()) {

                }
            });
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        connection.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket datagramPacket = (DatagramPacket) msg;
        //Packet packet = PacketDecode.(datagramPacket);

        datagramPacket.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }


    public UDPChannelHandler setMulticastAddress(InetAddress multicastAddress) {
        if (!multicastAddress.isMulticastAddress()) {
            throw new IllegalArgumentException(multicastAddress + "not a multicastAddress");
        }

        this.multicastAddress = multicastAddress;
        return this;
    }

    public UDPChannelHandler setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
        return this;
    }

    public Connection getConnection() {
        return connection;
    }
}
