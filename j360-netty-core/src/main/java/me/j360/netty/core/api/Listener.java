package me.j360.netty.core.api;

import java.util.Objects;

/**
 * @author: min_xu
 * @date: 2017/12/27 上午10:38
 * 说明：
 */
public interface Listener {

    void onSuccess(Objects... args);

    void onFailure(Throwable cause);
}
