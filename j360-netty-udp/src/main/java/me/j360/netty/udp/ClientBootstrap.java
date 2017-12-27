package me.j360.netty.udp;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午3:00
 * 说明：
 */
public class ClientBootstrap {

    public static void main(String[] args) {

        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", 8888), new File("/Users/min_xu/Documents/path_file.txt"));

        try {
            broadcaster.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            broadcaster.stop();
        }
    }
}
