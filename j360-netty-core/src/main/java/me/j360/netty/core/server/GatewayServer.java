package me.j360.netty.core.server;

import io.netty.channel.ChannelHandler;
import me.j360.netty.core.connection.ConnectionManager;
import me.j360.netty.core.handler.ServerChannelHandler;
import me.j360.netty.core.tcp.NettyTCPServer;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午5:15
 * 说明：
 */
public class GatewayServer extends NettyTCPServer {

    private ServerChannelHandler channelHandler;
    private ConnectionManager connectionManager;

    public GatewayServer(int port) {
        super(port);
        //this.connectionManager = new ServerConnectionManager(false);
        this.channelHandler = new ServerChannelHandler(connectionManager);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }
}
