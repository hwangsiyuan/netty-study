package com.hussein.netty.udp;

import com.hussein.netty.codec.LogFileEventEncoder;
import com.hussein.netty.model.LogFileEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * <p>Title: LogEventBroadcaster</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/9 7:34 PM
 */
public class LogEventBroadcaster {

    private EventLoopGroup group = new NioEventLoopGroup();

    private Bootstrap bootstrap;

    private final File file;

    public LogEventBroadcaster(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws Exception {
        URL url = LogEventBroadcaster.class.getClassLoader().getResource("mylog.log");
        if (url == null) {
            return;
        }
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new File(url.getFile()));
        // 向网段内的所有机器广播UDP消息
        broadcaster.start(new InetSocketAddress("255.255.255.255", 9090));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }

    private void run() throws Exception {
        Channel channel = bootstrap.bind(0).syncUninterruptibly().channel();
        long pointer = 0;
        while (true) {
            long len = file.length();
            if (len < pointer) {
                pointer = len;
            } else {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    channel.writeAndFlush(new LogFileEvent(file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    private void stop() throws InterruptedException {
        group.shutdownGracefully().sync();
    }


    private void start(InetSocketAddress address) {
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogFileEventEncoder(address));
    }
}
