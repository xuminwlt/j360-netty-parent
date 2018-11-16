package me.j360.netty.core.test;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import me.j360.netty.core.api.Listener;
import me.j360.netty.core.client.GatewayUDPConnector;
import me.j360.netty.core.client.GetawayClient;
import me.j360.netty.core.connection.Connection;
import me.j360.netty.core.protocol.Packet;
import me.j360.netty.core.server.GatewayServer;
import me.j360.netty.core.server.GetawayUDPConnection;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午5:46
 * 说明：
 */

@Slf4j
public class NettyTest {

    private final AttributeKey<String> attrKey = AttributeKey.valueOf("host_port");

    @Test
    public void serverStart() throws InterruptedException {
        System.setProperty("io.netty.leakDetection.level", "PARANOID");
        System.setProperty("io.netty.noKeySetOptimization", "false");
        log.info("server start");
        GatewayServer gatewayServer = new GatewayServer(8888);
        gatewayServer.init();

        gatewayServer.start(new Listener() {
            @Override
            public void onSuccess(Objects... args) {
                log.info("start {} success on:{}");
            }

            @Override
            public void onFailure(Throwable cause) {
                log.error("start failure, jvm exit with code -1");
                System.exit(-1);
            }
        });

        for (;;) {
            Thread.sleep(5000);
            int i = gatewayServer.getConnectionManager().getConnNum();
            System.out.println(i);
        }

        //LockSupport.park();
    }

    @Test
    public void clientStart() throws InterruptedException {
        log.info("client start");
        GetawayClient client = new GetawayClient();
        client.start().join();
        addConnection(client, "127.0.0.1" , 8888, true);
        addConnection(client, "127.0.0.1" , 8888, true);
        addConnection(client, "127.0.0.1" , 8888, true);
        //
        for (;;) {
            Thread.sleep(5000);
            int i = client.getConnectionManager().getConnNum();
            System.out.println(i);

            if (i >= 0) {
                Connection connection = client.getConnectionManager().getFirst();
                System.out.println(connection.getId().toString());
                connection.getChannel().writeAndFlush(Unpooled.copiedBuffer("hello server, loop...", CharsetUtil.UTF_8));
            }
        }
        //LockSupport.park();
    }


    @Test
    public void udpConnectTest() {
        GetawayUDPConnection udpServer = new GetawayUDPConnection(3003);
        udpServer.init();
        udpServer.start(new Listener() {
            @Override
            public void onSuccess(Objects... args) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

        LockSupport.park();
    }


    @Test
    public void udpClientTest() {
        GatewayUDPConnector connector = new GatewayUDPConnector(3004);
        connector.init();
        connector.start(new Listener() {
            @Override
            public void onSuccess(Objects... args) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

        //
        Connection connection = connector.getConnection();
        Packet packet = new Packet();
        packet.setRecipient(new InetSocketAddress("127.0.0.1", 3003));
        connection.send(packet, f -> {
            if (f.isSuccess()) {
                System.out.println("send broadcast to gateway server success, userId={}, conn={}");
            } else {
                System.out.println("send broadcast to gateway server failure, userId={}, conn={}");
            }
        });
    }



    private void addConnection(GetawayClient client, String host, int port, boolean sync) {
        ChannelFuture future = client.connect(host, port, new Listener() {
            @Override
            public void onSuccess(Objects... args) {
                log.info("success");
            }

            @Override
            public void onFailure(Throwable cause) {
                log.error("error", cause);
            }
        });
        future.channel().attr(attrKey).set(getHostAndPort(host, port));
        future.addListener(f -> {
            if (!f.isSuccess()) {
                log.error("create gateway connection failure, host={}, port={}", host, port, f.cause());
            }
        });
        if (sync) future.awaitUninterruptibly();
    }

    private static String getHostAndPort(String host, int port) {
        return host + ":" + port;
    }
}
