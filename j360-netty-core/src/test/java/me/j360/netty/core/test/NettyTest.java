package me.j360.netty.core.test;

import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import me.j360.netty.core.api.Listener;
import me.j360.netty.core.client.GetawayClient;
import me.j360.netty.core.server.GatewayServer;
import org.junit.Test;

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
    public void serverStart() {
        GatewayServer gatewayServer = new GatewayServer();
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

        LockSupport.park();
    }

    @Test
    public void clientStart() {

        GetawayClient client = new GetawayClient();
        client.start().join();


        addConnection(client, "localhost" , 8888, false);
    }

    private void addConnection(GetawayClient client, String host, int port, boolean sync) {
        ChannelFuture future = client.connect(host, port);
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
