package me.j360.netty.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import me.j360.netty.core.api.Listener;
import me.j360.netty.core.connection.Connection;
import me.j360.netty.core.udp.NettyUDPConnector;
import me.j360.netty.core.udp.UDPChannelHandler;
import me.j360.netty.core.util.Utils;


public final class GatewayUDPConnector extends NettyUDPConnector {

    private UDPChannelHandler channelHandler;

    public GatewayUDPConnector(int port) {
        super(port);
    }

    @Override
    public void init() {
        super.init();
        channelHandler = new UDPChannelHandler();
        channelHandler.setMulticastAddress(Utils.getInetAddress("239.239.239.99"));
        channelHandler.setNetworkInterface(Utils.getLocalNetworkInterface());
    }


    @Override
    public void stop(Listener listener) {
        super.stop(listener);
    }


    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        b.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);
        b.option(ChannelOption.IP_MULTICAST_TTL, 255);
        //if (snd_buf.gateway_client > 0) b.option(ChannelOption.SO_SNDBUF, snd_buf.gateway_client);
        //if (rcv_buf.gateway_client > 0) b.option(ChannelOption.SO_RCVBUF, rcv_buf.gateway_client);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public Connection getConnection() {
        return channelHandler.getConnection();
    }

}
