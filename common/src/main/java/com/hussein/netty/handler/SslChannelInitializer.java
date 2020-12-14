package com.hussein.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * <p>Title: SslChannelInitializer</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/29 3:42 PM
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SSLContext sslContext;

    private final boolean client;

    private final boolean startTls;

    public SslChannelInitializer(SSLContext sslContext, boolean client, boolean startTls) {
        this.sslContext = sslContext;
        this.client = client;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(client);
        ch.pipeline().addFirst(new SslHandler(sslEngine, startTls));
    }
}
