package me.j360.netty.udp.multicast.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.Builder;
import lombok.Data;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2018/11/16 3:59 PM
 * 说明：
 */

@Builder
@Data
public class UDPConnection {

    private Channel channel;
    private InetSocketAddress address;


    public void send(String content) {
        if (channel.isActive()) {
            DatagramPacket packet = new DatagramPacket(
                    Unpooled.copiedBuffer(content, CharsetUtil.UTF_8),
                    address);
            ChannelFuture future = channel.writeAndFlush(packet).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                }
            });
        }
    }

}
