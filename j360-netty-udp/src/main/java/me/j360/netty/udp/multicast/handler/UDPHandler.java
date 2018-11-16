package me.j360.netty.udp.multicast.handler;

import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Objects;

/**
 * @author: min_xu
 * @date: 2018/11/16 2:55 PM
 * 说明：
 */

@Slf4j
@ChannelHandler.Sharable
public class UDPHandler extends ChannelInboundHandlerAdapter {

    private int port;
    private InetAddress multicastAddress;
    private NetworkInterface networkInterface;
    private final UDPConnection connection = UDPConnection.builder().build();;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (Objects.nonNull(multicastAddress)) {
            ((DatagramChannel) (ctx.channel())).joinGroup(multicastAddress, networkInterface, null).addListener(future -> {
                if (future.isSuccess()) {
                    connection.setChannel(ctx.channel());
                } else {

                }
            });
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }


    public UDPHandler setMulticastAddress(InetAddress multicastAddress) {
        if (!multicastAddress.isMulticastAddress()) {
            throw new IllegalArgumentException(multicastAddress + "not a multicastAddress");
        }

        this.multicastAddress = multicastAddress;
        InetSocketAddress socketAddress = new InetSocketAddress(multicastAddress, port);
        this.connection.setAddress(socketAddress);
        return this;
    }

    public UDPHandler setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
        return this;
    }

    public UDPConnection getConnection() {
        return connection;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
