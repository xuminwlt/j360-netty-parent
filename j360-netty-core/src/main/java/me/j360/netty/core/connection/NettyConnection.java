package me.j360.netty.core.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import me.j360.netty.core.protocol.Packet;

import java.util.Objects;

/**
 * @author: min_xu
 * @date: 2017/12/27 下午4:53
 * 说明：
 */
public final class NettyConnection implements Connection, ChannelFutureListener {

    //private static final Cipher RSA_CIPHER =

    private SessionContext context;
    private Channel channel;
    private volatile byte status = STATUS_NEW;
    private long lastReadTime;
    private long lastWriteTime;


    @Override
    public void init(Channel channel, boolean security) {
        this.channel = channel;
        this.context = new SessionContext();
        this.lastReadTime = System.currentTimeMillis();
        this.status = STATUS_CONNECTION;
        if (security) {

        }
    }

    @Override
    public ChannelFuture send(Packet packet) {
        return send(packet, null);
    }

    @Override
    public ChannelFuture send(Packet packet, ChannelFutureListener listener) {
        if (channel.isActive()) {
            ChannelFuture future = channel.writeAndFlush(packet.toFrame(channel)).addListener(this);
            if (Objects.nonNull(listener)) {
                future.addListener(listener);
            }
            if (channel.isWritable()) {
                return future;
            }

            if (future.channel().eventLoop().inEventLoop()) {
                future.awaitUninterruptibly(100);
            }
            return future;
        }

        return this.close();
    }

    @Override
    public SessionContext getSessionContext() {
        return context;
    }

    @Override
    public void setSessionContext(SessionContext sessionContext) {
        this.context = sessionContext;
    }

    @Override
    public String getId() {
        return channel.id().asShortText();
    }

    @Override
    public ChannelFuture close() {
        if (status == STATUS_DISCONNECTION) return null;
        this.status = STATUS_DISCONNECTION;
        return this.channel.close();
    }

    @Override
    public boolean isConnected() {
        return status == STATUS_CONNECTION;
    }

    @Override
    public boolean isReadTimeout() {
        return System.currentTimeMillis() - lastReadTime > context.heartbeat;
    }

    @Override
    public boolean isWriteTimeout() {
        return System.currentTimeMillis() - lastWriteTime > context.heartbeat;
    }

    @Override
    public void updateLastReadTime() {
        this.lastReadTime = System.currentTimeMillis();
    }

    @Override
    public void updateLastWriteTime() {
        this.lastWriteTime = System.currentTimeMillis();
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {

    }

    @Override
    public String toString() {
        return "[channel=" + channel
                + ", context=" + context
                + ", status=" + status
                + ", lastReadTime=" + lastReadTime
                + ", lastWriteTime=" + lastWriteTime
                + "]";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NettyConnection that = (NettyConnection) o;
        return channel.id().equals(that.channel.id());
    }

    public int hashcode() {
        return channel.id().hashCode();
    }

}
