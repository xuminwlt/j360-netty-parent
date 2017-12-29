package me.j360.netty.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import me.j360.netty.core.api.Listener;
import me.j360.netty.core.connection.ConnectionManager;
import me.j360.netty.core.connection.NettyConnectionManager;
import me.j360.netty.core.handler.EchoClientHandler;
import me.j360.netty.core.tcp.NettyTCPClient;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午5:15
 * 说明：
 */
public class GetawayClient extends NettyTCPClient {

    private final EchoClientHandler handler;
    private GlobalChannelTrafficShapingHandler trafficShapingHandler;
    private ScheduledExecutorService trafficShapingExecutor;
    private final ConnectionManager connectionManager;


    public GetawayClient() {
        //messageDispatcher.register(Command.OK, () -> new GatewayOKHandler(mPushClient));
        //messageDispatcher.register(Command.ERROR, () -> new GatewayErrorHandler(mPushClient));
        connectionManager = new NettyConnectionManager();
        handler = new EchoClientHandler(connectionManager);

    }

    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        super.initPipeline(pipeline);
        /*if (trafficShapingHandler != null) {
            pipeline.addLast(trafficShapingHandler);
        }*/
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {

        super.doStop(listener);
    }

    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        //if (snd_buf.gateway_client > 0) b.option(ChannelOption.SO_SNDBUF, snd_buf.gateway_client);
        //if (rcv_buf.gateway_client > 0) b.option(ChannelOption.SO_RCVBUF, rcv_buf.gateway_client);
    }

    @Override
    public ChannelFactory<? extends Channel> getChannelFactory() {
        return super.getChannelFactory();
    }

    @Override
    public SelectorProvider getSelectorProvider() {
        return super.getSelectorProvider();
    }

    @Override
    protected int getWorkThreadNum() {
        return 4;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

}
