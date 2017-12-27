package me.j360.netty.udp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午3:01
 * 说明：
 */

@AllArgsConstructor
@Data
public class LogEvent {

    public static final byte SEPARATOR = (byte) ';';

    private final InetSocketAddress source;
    private final long received;
    private final String logfile;
    private final String msg;

    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

}
