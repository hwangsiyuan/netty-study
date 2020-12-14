package com.hussein.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * <p>Title: SecureChatServerIntializer</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/2 4:45 PM
 */
public class SecureChatServerIntializer extends ChatServerInitializer {

    private final ChannelGroup channelGroup;

    private final SSLContext sslContext;

    public SecureChatServerIntializer(ChannelGroup channelGroup,SSLContext sslContext) {
        super(channelGroup);
        this.channelGroup = channelGroup;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        ch.pipeline().addLast(new SslHandler(engine));
    }
}
