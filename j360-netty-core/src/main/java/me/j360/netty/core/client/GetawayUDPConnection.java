package me.j360.netty.core.client;

import io.netty.channel.ChannelHandler;
import me.j360.netty.core.udp.NettyUDPConnector;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午4:29
 * 说明：
 */
public final class GetawayUDPConnection extends NettyUDPConnector {
    public GetawayUDPConnection(int port) {
        super(port);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return null;
    }
}
