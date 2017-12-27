package me.j360.netty.core.service;

import me.j360.netty.core.api.Listener;
import me.j360.netty.core.tcp.BaseService;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: min_xu
 * @date: 2017/12/27 上午10:50
 * 说明：
 */
public class FutureListener extends CompletableFuture<Boolean> implements Listener {


    private final Listener listener;
    private final AtomicBoolean started;

    public FutureListener(AtomicBoolean started) {
        this.started = started;
        this.listener = null;
    }

    public FutureListener(Listener listener, AtomicBoolean started) {
        this.listener = listener;
        this.started = started;
    }


    @Override
    public void onSuccess(Objects... args) {
        if (isDone()) return;
        complete(started.get());
        if (Objects.nonNull(listener)) {
            listener.onSuccess(args);
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        if (isDone()) return;
        completeExceptionally(cause);
        if (Objects.nonNull(listener)) {
            listener.onFailure(cause);
        }
        //throw cause instanceof ServiceEx
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void monitor(BaseService service) {
        if (isDone()) return;
        runAsync(() -> {

        });
    }
}
