package me.j360.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import me.j360.netty.udp.codec.LogEventEncoder;
import me.j360.netty.udp.domain.LogEvent;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * @author: min_xu
 * @date: 2017/12/26 下午4:05
 * 说明：
 */
public class LogEventBroadcaster {


    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true).handler(new LogEventEncoder(address));
        this.file = file;
    }


    public void run() throws InterruptedException, IOException {
        Channel channel = bootstrap.bind(0).sync().channel();
        long pointer = 0L;

        for (;;) {
            long len = file.length();
            if (len < pointer) {
                pointer = len;
            } else if (len > pointer) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(pointer);
                String line;
                while ((line = randomAccessFile.readLine()) != null) {
                    channel.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }

                pointer = randomAccessFile.getFilePointer();
                randomAccessFile.close();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }
}
