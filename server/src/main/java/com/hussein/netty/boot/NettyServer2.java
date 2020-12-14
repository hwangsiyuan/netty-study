package com.hussein.netty.boot;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * <p>Title: NettyServer2</p>
 * <p>Description: 服务启动时，先去加载远程数据成功时做后续处理</p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/1 5:50 PM
 */
public class NettyServer2 {

    public static void main(String[] args) throws Exception {
        new NettyServer2().start();
    }

    private void start() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {

                        ChannelFuture connectFuture;

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            super.channelActive(ctx);
                            Bootstrap client = new Bootstrap();
                            client.group(ctx.channel().eventLoop())
                                    .channel(NioSocketChannel.class)
                                    .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                                            System.out.println("Reveived data");
                                            in.clear();
                                        }
                                    });
                            connectFuture = client.connect(new InetSocketAddress("www.google.com", 80));
                            System.out.println(connectFuture);
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            //远程处理成功时才处理后续
                            if (connectFuture.isSuccess()) {

                            }
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            super.exceptionCaught(ctx, cause);
                            countDownLatch.countDown();
                        }
                    });
            ChannelFuture channelFuture = server.bind(new InetSocketAddress(8070));
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound..");
                } else {
                    System.err.println("Bound attempt faild..");
                    channelFuture.cause().printStackTrace();
                }
            });
            countDownLatch.await();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }
    }
}
