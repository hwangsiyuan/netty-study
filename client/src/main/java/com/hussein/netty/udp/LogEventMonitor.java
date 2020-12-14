package com.hussein.netty.udp;

import com.hussein.netty.codec.LogFileEventDecoder;
import com.hussein.netty.handler.LogEventHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * <p>Title: LogEventMonitor</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/10 10:43 AM
 */
public class LogEventMonitor {

    private EventLoopGroup group = new NioEventLoopGroup();

    private Bootstrap bootstrap;

    public static void main(String[] args) throws InterruptedException {
        LogEventMonitor logEventMonitor = new LogEventMonitor();
        logEventMonitor.start();
        try {
            Channel channel = logEventMonitor.bind();
            channel.closeFuture().await();
        } finally {
            logEventMonitor.stop();
        }
    }

    private Channel bind() {
        return bootstrap.bind(9090).syncUninterruptibly().channel();
    }

    private void stop() {
        group.shutdownGracefully();
    }

    private void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(
                                new LogFileEventDecoder(),
                                new LogEventHandler()
                        );
                    }
                });
    }
}
