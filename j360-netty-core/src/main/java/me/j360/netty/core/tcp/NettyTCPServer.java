package me.j360.netty.core.tcp;

import io.netty.channel.EventLoopGroup;
import me.j360.netty.core.api.Server;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午7:59
 * 说明：
 */
public abstract class NettyTCPServer implements Server {

    public enum State{ Created, Initialized, Starting, Started, Shutdown}

    protected final AtomicReference<State> serverState = new AtomicReference<>(State.Created);

    protected final int port;
    protected final String host;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workGroup;

    public NettyTCPServer(int port) {
        this.port = port;
        this.host = null;
    }

    public NettyTCPServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void init() {
        if (!serverState.compareAndSet(State.Created, State.Initialized)) {

        }
    }

    @Override
    public boolean isRunning() {
        return serverState.get() == State.Started;
    }



    private void createServer() {

    }



    //#############################
    //可选参数的get方法
    //#############################


}
