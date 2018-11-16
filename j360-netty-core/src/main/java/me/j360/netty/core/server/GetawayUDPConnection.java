package me.j360.netty.core.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import me.j360.netty.core.connection.Connection;
import me.j360.netty.core.udp.NettyUDPConnector;
import me.j360.netty.core.udp.UDPChannelHandler;
import me.j360.netty.core.util.Utils;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午4:27
 * 说明：
 */
public final class GetawayUDPConnection extends NettyUDPConnector {

    private UDPChannelHandler channelHandler;

    public GetawayUDPConnection(int port) {
        super(port);
        this.channelHandler = new UDPChannelHandler();
    }


    @Override
    public void init() {
        super.init();
        channelHandler.setMulticastAddress(Utils.getInetAddress("239.239.239.88"));
        channelHandler.setNetworkInterface(Utils.getLocalNetworkInterface());
    }

    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        b.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);//默认情况下，当本机发送组播数据到某个网络接口时，在IP层，数据会回送到本地的回环接口，选项IP_MULTICAST_LOOP用于控制数据是否回送到本地的回环接口
        b.option(ChannelOption.IP_MULTICAST_TTL, 255);//选项IP_MULTICAST_TTL允许设置超时TTL，范围为0～255之间的任何值
        //b.option(ChannelOption.IP_MULTICAST_IF, null);//选项IP_MULTICAST_IF用于设置组播的默认网络接口，会从给定的网络接口发送，另一个网络接口会忽略此数据,参数addr是希望多播输出接口的IP地址，使用INADDR_ANY地址回送到默认接口。
        //b.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(32 * 1024, 1024 * 1024));
        //if (snd_buf.gateway_server > 0) b.option(ChannelOption.SO_SNDBUF, snd_buf.gateway_server);
        //if (rcv_buf.gateway_server > 0) b.option(ChannelOption.SO_RCVBUF, rcv_buf.gateway_server);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public Connection getConnection() {
        return channelHandler.getConnection();
    }

}
