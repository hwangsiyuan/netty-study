package com.hussein.netty.boot;

import com.hussein.netty.handler.SpdyChannelInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * <p>Title: SpdyHttpServer</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/8 11:39 AM
 */
public class SpdyHttpServer {

    private EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    public static void main(String[] args) throws Exception {
        new SpdyHttpServer().start();
    }

    private void destroy() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    private void start() throws Exception {
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContext.newServerContext(cert.certificate(), cert.privateKey());
        ServerBootstrap server = new ServerBootstrap();
        server.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SpdyChannelInitalizer(context));
        ChannelFuture future = server.bind(new InetSocketAddress(8080));
        this.channel = future.channel();
        future.syncUninterruptibly();
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }
}
