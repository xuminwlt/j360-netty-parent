package me.j360.netty.nio;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: min_xu
 * @date: 2018/10/25 上午11:40
 * 说明：
 */
public abstract class AbstractNioSelector implements Runnable {

    private final Executor executor;

    protected Selector selector;

    protected final AtomicBoolean wakeUp = new AtomicBoolean();

    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

    private String threadName;

    protected NioSelectorRunnablePool selectorRunnablePool;

    AbstractNioSelector(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        this.executor = executor;
        this.threadName = threadName;
        this.selectorRunnablePool = selectorRunnablePool;

        openSelector();
    }


    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);

        while (true) {
            try {
                wakeUp.set(false);
                select(selector);
                processTaskQueue();
                process(selector);
            } catch (Exception e) {

            }
        }
    }


    private void openSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        executor.execute(this);
    }


    protected final void registerTask(Runnable task) {
        taskQueue.add(task);
        Selector selector = this.selector;
        if (selector != null) {
            if (wakeUp.compareAndSet(false, true)) {
                selector.wakeup();
            }
        } else {
            taskQueue.remove(task);
        }
    }
    private void processTaskQueue() {
        for (;;) {
            final Runnable task = taskQueue.poll();
            if (task == null) {
                break;
            }
            task.run();
        }
    }

    protected abstract int select(Selector selector) throws IOException;

    protected abstract  void process(Selector selector) throws IOException;

    public NioSelectorRunnablePool getSelectorRunnablePool() {
        return selectorRunnablePool;
    }
}
