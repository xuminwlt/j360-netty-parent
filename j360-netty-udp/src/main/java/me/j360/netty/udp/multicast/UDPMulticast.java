package me.j360.netty.udp.multicast;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.util.NetUtil;
import me.j360.netty.udp.multicast.handler.UDPHandler;

import java.net.*;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author: min_xu
 * @date: 2018/11/16 3:18 PM
 * 说明：
 */
public class UDPMulticast extends AbstractSimpleServer {

    private UDPHandler udpHandler;
    private InetAddress localAddress = null;

    public UDPMulticast(int port) throws UnknownHostException {
        super(port);
        NetworkInterface ni = NetUtil.LOOPBACK_IF;
        Enumeration<InetAddress> addresses = ni.getInetAddresses();

        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address instanceof Inet4Address){
                localAddress = address;
            }
        }

        this.udpHandler = new UDPHandler();
        udpHandler.setMulticastAddress(InetAddress.getByName("224.0.0.5"));
        udpHandler.setPort(port);
        udpHandler.setNetworkInterface(ni);

    }

    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        b.localAddress(localAddress, port);
        b.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);
        b.option(ChannelOption.IP_MULTICAST_TTL, 255);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return udpHandler;
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


    public void send(String content) {
        this.udpHandler.getConnection().send(content);
    }

    public void send(String content, Function<String, UDPHandler> creater, Consumer<UDPHandler> sender) {
        sender.accept(creater.apply(content));
    }
}
