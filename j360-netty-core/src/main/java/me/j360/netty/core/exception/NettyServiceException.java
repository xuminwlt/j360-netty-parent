package me.j360.netty.core.exception;

/**
 * @author: min_xu
 * @date: 2017/12/27 下午3:43
 * 说明：
 */
public class NettyServiceException extends RuntimeException {
    public NettyServiceException(String message) {
        super(message);
    }

    public NettyServiceException(Throwable cause) {
        super(cause);
    }

    public NettyServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
