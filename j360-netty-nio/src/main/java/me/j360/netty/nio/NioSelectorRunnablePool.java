package me.j360.netty.nio;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: min_xu
 * @date: 2018/10/25 上午11:39
 * 说明：
 */
public class NioSelectorRunnablePool {

    private final AtomicInteger bossIndex = new AtomicInteger();
    private Boss[] bosses;

    private final AtomicInteger workerIndex = new AtomicInteger();
    private Worker[] workers;

    public NioSelectorRunnablePool(Executor boss, Executor worker) {
        initBoss(boss, 1);
        initWorker(worker, Runtime.getRuntime().availableProcessors() * 2);
    }

    private void initBoss(Executor boss, int count) {
        this.bosses = new NioServerBoss[count];
        for (int i = 0; i < bosses.length; i++) {
            bosses[i] = new NioServerBoss(boss, "boss-hread" + (i+1), this);
        }
    }

    private void initWorker(Executor worker, int count) {
        this.workers = new NioServerWorker[count];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new NioServerWorker(worker, "worker-thread" + (i+1), this);
        }
    }

    public Worker nextWorker() {
        return workers[Math.abs(workerIndex.getAndIncrement() % workers.length)];
    }

    public Boss nextBoss() {
        return bosses[Math.abs(bossIndex.getAndIncrement() % bosses.length)];
    }
}
