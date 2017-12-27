package me.j360.netty.core.tcp;

import me.j360.netty.core.api.Listener;
import me.j360.netty.core.api.Service;
import me.j360.netty.core.service.FutureListener;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: min_xu
 * @date: 2017/12/27 上午10:40
 * 说明：
 */
public abstract class BaseService implements Service {

    protected final AtomicBoolean started = new AtomicBoolean();

    @Override
    public void start(Listener listener) {
        tryStart(listener, this::doStart);
    }

    @Override
    public void stop(Listener listener) {
        tryStop(listener, this::doStop);
    }

    @Override
    public final CompletableFuture<Boolean> start() {
        FutureListener listener = new FutureListener(started);
        start(listener);
        return listener;
    }

    @Override
    public final CompletableFuture<Boolean> stop() {
        FutureListener listener = new FutureListener(started);
        stop(listener);
        return listener;
    }

    @Override
    public final boolean syncStart() {
        return start().join();
    }

    @Override
    public final boolean syncStop() {
        return stop().join();
    }

    @Override
    public void init() {

    }

    @Override
    public boolean isRunning() {
        return started.get();
    }


    protected void doStart(Listener listener) throws Throwable {
        listener.onSuccess();
    }

    protected void doStop(Listener listener) throws Throwable {
        listener.onSuccess();
    }

    protected void tryStart(Listener l, FunctionEx functionEx) {
        FutureListener listener = wrap(l);
        if (started.compareAndSet(false, true)) {
            try {
                init();
                functionEx.apply(l);
                listener.monitor(this);
            } catch (Throwable throwable) {
                listener.onFailure(throwable);
                throwable.printStackTrace();
            }
        }
    }

    protected void tryStop(Listener l, FunctionEx functionEx) {
        FutureListener listener = wrap(l);
        if (started.compareAndSet(true, false)) {
            try {
                functionEx.apply(l);
                listener.monitor(this);
            } catch (Throwable throwable) {
                listener.onFailure(throwable);
                throwable.printStackTrace();
            } ;
        }
    }


    protected interface FunctionEx {
        void apply(Listener l) throws Throwable;
    }

    public FutureListener wrap(Listener l) {
        if (Objects.isNull(l)) {
            return new FutureListener(started);
        }
        if (l instanceof FutureListener) {
            return (FutureListener) l;
        }
        return new FutureListener(l, started);
    }
}
