package me.j360.netty.core.api;

import java.util.concurrent.CompletableFuture;

/**
 * @author: min_xu
 * @date: 2017/12/27 上午10:38
 * 说明：
 */
public interface Service {

    void start(Listener listener);

    void stop(Listener listener);

    CompletableFuture<Boolean> start();

    CompletableFuture<Boolean> stop();

    boolean syncStart();

    boolean syncStop();

    void init();

    boolean isRunning();
}
