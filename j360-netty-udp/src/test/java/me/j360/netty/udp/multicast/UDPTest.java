package me.j360.netty.udp.multicast;

import org.junit.Test;

import java.net.UnknownHostException;

/**
 * @author: min_xu
 * @date: 2018/11/16 3:16 PM
 * 说明：
 */
public class UDPTest {

    @Test
    public void receiverTest() throws UnknownHostException {
        UDPReceiver udpReceiver = new UDPReceiver(1234);
        udpReceiver.start();
    }

    @Test
    public void senderTest() throws UnknownHostException {
        UDPMulticast udpMulticast = new UDPMulticast(1234);
        udpMulticast.start();
        String content = "aaaa";

        udpMulticast.send(content);

    }

}
