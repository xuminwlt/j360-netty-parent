package me.j360.netty.udp.multicast;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.util.NetUtil;
import me.j360.netty.udp.multicast.handler.UDPHandler;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author: min_xu
 * @date: 2018/11/16 2:48 PM
 * 说明：
 */
public class UDPReceiver extends AbstractSimpleServer {


    private UDPHandler udpHandler;
    private InetAddress localAddress = null;

    protected UDPReceiver(int port) throws UnknownHostException {
        super(port);
        this.udpHandler = new UDPHandler();

        NetworkInterface ni = NetUtil.LOOPBACK_IF;
        Enumeration<InetAddress> addresses = ni.getInetAddresses();

        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address instanceof Inet4Address){
                localAddress = address;
            }
        }

        udpHandler.setMulticastAddress(InetAddress.getByName("224.0.0.5"));
        udpHandler.setNetworkInterface(ni);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return udpHandler;
    }

    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        b.localAddress(localAddress, port);
        b.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);//默认情况下，当本机发送组播数据到某个网络接口时，在IP层，数据会回送到本地的回环接口，选项IP_MULTICAST_LOOP用于控制数据是否回送到本地的回环接口
        b.option(ChannelOption.IP_MULTICAST_TTL, 255);//选项IP_MULTICAST_TTL允许设置超时TTL，范围为0～255之间的任何值
        //b.option(ChannelOption.IP_MULTICAST_IF, null);//选项IP_MULTICAST_IF用于设置组播的默认网络接口，会从给定的网络接口发送，另一个网络接口会忽略此数据,参数addr是希望多播输出接口的IP地址，使用INADDR_ANY地址回送到默认接口。
        //b.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(32 * 1024, 1024 * 1024));
    }

    @Override
    public void start() {
        super.createNioServer(new Listener() {
            @Override
            public void onSuccess(Object... args) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }

    @Override
    public void stop() {

    }
}
