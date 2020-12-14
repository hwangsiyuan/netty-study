package com.hussein.netty.boot;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * <p>Title: NettyClient3</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/1 8:16 PM
 */
public class NettyClient3 {

    public static void main(String[] args) throws Exception {
        new NettyClient3().start();
    }

    private void start() throws Exception {
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.group(group)
                    .channel(OioDatagramChannel.class)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {

                        }
                    });
            ChannelFuture channelFuture = client.bind(new InetSocketAddress(0));
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println("channel bound");
                } else {
                    System.out.println("bound attempt faild");
                }
            });
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
