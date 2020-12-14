package com.hussein.netty.boot;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Random;

/**
 * <p>Title: NettyClient2</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/1 7:34 PM
 */
public class NettyClient2 {

    private final AttributeKey<Integer> id = AttributeKey.newInstance("ID");

    public static void main(String[] args) throws Exception {
        new NettyClient2().start();
    }

    private void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            startClient(group);
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private void startClient(EventLoopGroup group) throws Exception {
        Bootstrap client = new Bootstrap();
        client.group(group)
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelRegistered(ctx);
                        int attrId = ctx.channel().attr(id).get();
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Receive data");
                        msg.clear();
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .attr(id, new Random().nextInt());
        System.out.println(client.toString());
        ChannelFuture channelFuture = client.connect(new InetSocketAddress("127.0.0.1", 8070));
        channelFuture.channel().closeFuture().sync();
    }
}
