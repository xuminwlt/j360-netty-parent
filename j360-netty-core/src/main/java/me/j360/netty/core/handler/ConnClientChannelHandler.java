package me.j360.netty.core.handler;

import com.sun.deploy.config.ClientConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.*;
import lombok.extern.slf4j.Slf4j;
import me.j360.netty.core.client.TestStatistics;
import me.j360.netty.core.connection.Connection;
import me.j360.netty.core.connection.NettyConnection;
import me.j360.netty.core.constants.Command;
import me.j360.netty.core.constants.ThreadNames;
import me.j360.netty.core.protocol.Packet;
import me.j360.netty.core.util.NamedPoolThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午6:46
 * 说明：
 */

@Slf4j
public class ConnClientChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Timer HASHED_WHEEL_TIMER = new HashedWheelTimer(new NamedPoolThreadFactory(ThreadNames.T_CONN_TIMER));
    public static final AttributeKey<ClientConfig> CONFIG_KEY = AttributeKey.newInstance("clientConfig");
    public static final TestStatistics STATISTICS = new TestStatistics();
    private final Connection connection = new NettyConnection();

    private boolean perfTest;
    private int hbTimeoutTimes;


    public ConnClientChannelHandler() {

    }

    public Connection getConnection() {
        return connection;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        connection.updateLastReadTime();
        if (msg instanceof Packet) {
            Packet packet = (Packet) msg;
            Command command = Command.toCMD(packet.cmd);
            //TODO

        }

        log.debug("receive package={}, chanel={}", msg, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        connection.close();
        log.error("caught an ex, channel={}", ctx.channel(), cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int clientNum = STATISTICS.clientNum.incrementAndGet();
        log.info("client connect channel={}, clientNum={}", ctx.channel(), clientNum);

        /*for (int i = 0; i < 3; i++) {
            if (clientConfig != null) break;
            clientConfig = ctx.channel().attr(CONFIG_KEY).getAndSet(null);
            if (clientConfig == null) TimeUnit.SECONDS.sleep(1);
        }

        if (clientConfig == null) {
            throw new NullPointerException("client config is null, channel=" + ctx.channel());
        }*/

        connection.init(ctx.channel(), true);
        if (perfTest) {

        } else {
            tryFastConnect();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int clientNum = STATISTICS.clientNum.decrementAndGet();
        connection.close();

        log.info("client disconnect channel={}, clientNum={}", connection, clientNum);
    }

    private void tryFastConnect() {

        /*Map<String, String> sessionTickets = getFastConnectionInfo(clientConfig.getDeviceId());

        if (sessionTickets == null) {
            handshake();
            return;
        }
        String sessionId = sessionTickets.get("sessionId");
        if (sessionId == null) {
            handshake();
            return;
        }
        String expireTime = sessionTickets.get("expireTime");
        if (expireTime != null) {
            long exp = Long.parseLong(expireTime);
            if (exp < System.currentTimeMillis()) {
                handshake();
                return;
            }
        }

        final String cipher = sessionTickets.get("cipherStr");*/

        /*FastConnectMessage message = new FastConnectMessage(connection);
        message.deviceId = clientConfig.getDeviceId();
        message.sessionId = sessionId;

        message.sendRaw(channelFuture -> {
            if (channelFuture.isSuccess()) {
                clientConfig.setCipher(cipher);
            } else {
                handshake();
            }
        });
        log.debug("send fast connect message={}", message);*/
    }



    private void startHeartBeat(final int heartbeat) throws Exception {
        HASHED_WHEEL_TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                if (connection.isConnected() && healthCheck()) {
                    HASHED_WHEEL_TIMER.newTimeout(this, heartbeat, TimeUnit.MILLISECONDS);
                }
            }
        }, heartbeat, TimeUnit.MILLISECONDS);
    }

    private boolean healthCheck() {

        if (connection.isReadTimeout()) {
            hbTimeoutTimes++;
            log.warn("heartbeat timeout times={}, client={}", hbTimeoutTimes, connection);
        } else {
            hbTimeoutTimes = 0;
        }

        if (hbTimeoutTimes >= 2) {
            log.warn("heartbeat timeout times={} over limit={}, client={}", hbTimeoutTimes, 2, connection);
            hbTimeoutTimes = 0;
            connection.close();
            return false;
        }

        if (connection.isWriteTimeout()) {
            log.info("send heartbeat ping...");

        }

        return true;
    }
}
