package com.hussein.netty.handler;

import com.hussein.netty.provider.DefaultServerProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import org.eclipse.jetty.npn.NextProtoNego;

import javax.net.ssl.SSLEngine;

/**
 * <p>Title: SpdyChannelInitalizer</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/8 11:22 AM
 */
public class SpdyChannelInitalizer extends ChannelInitializer<Channel> {

    private final SslContext context;

    public SpdyChannelInitalizer(SslContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine sslEngine = context.newEngine(ch.alloc());
        NextProtoNego.put(sslEngine, new DefaultServerProvider());
        NextProtoNego.debug = true;
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("ssl", new SslHandler(sslEngine));
        pipeline.addLast("spdyOrHttp", new SpdyOrHttpHandler("http/1.1"));
    }
}
