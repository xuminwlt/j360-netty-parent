package me.j360.netty.udp.multicast;

public interface Listener {
    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}