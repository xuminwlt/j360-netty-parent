package me.j360.netty.core.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: min_xu
 * @date: 2017/12/28 下午6:54
 * 说明：
 */
public class TestStatistics {

    public AtomicInteger clientNum = new AtomicInteger();
    public AtomicInteger connectedNum = new AtomicInteger();
    public AtomicInteger bindUserNum = new AtomicInteger();
    public AtomicInteger receivePushNum = new AtomicInteger();

    @Override
    public String toString() {
        return "TestStatistics{" +
                "clientNum=" + clientNum +
                ", connectedNum=" + connectedNum +
                ", bindUserNum=" + bindUserNum +
                ", receivePushNum=" + receivePushNum +
                '}';
    }
}
