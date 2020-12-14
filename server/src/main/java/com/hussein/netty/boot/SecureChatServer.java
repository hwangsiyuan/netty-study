package com.hussein.netty.boot;

import com.hussein.netty.handler.SecureChatServerIntializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;


/**
 * <p>Title: SecureChatServer</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/2 4:07 PM
 */
public class SecureChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    private ChannelFuture start(InetSocketAddress socketAddress) throws Exception {
        ServerBootstrap server = new ServerBootstrap();
        server.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SecureChatServerIntializer(channelGroup, SSLContext.getInstance("SSL", "SunJSSE")));
        ChannelFuture channelFuture = server.bind(socketAddress);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
        return channelFuture;
    }

    private void destroy() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        final SecureChatServer chatServer = new SecureChatServer();
        ChannelFuture channelFuture = chatServer.start(new InetSocketAddress(8070));
        Runtime.getRuntime().addShutdownHook(new Thread(chatServer::destroy));
        channelFuture.channel().closeFuture().syncUninterruptibly();
    }


}
