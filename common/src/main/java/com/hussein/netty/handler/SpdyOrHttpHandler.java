package com.hussein.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

/**
 * <p>Title: SpdyOrHttpHandler</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/8 12:17 PM
 */
public class SpdyOrHttpHandler extends ApplicationProtocolNegotiationHandler {

    public SpdyOrHttpHandler(String fallbackProtocol) {
        super(fallbackProtocol);
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext ctx, String protocol) throws Exception {
        ChannelPipeline pipeline = ctx.pipeline();
        if (protocol == null) {
            pipeline.addLast(new HttpSuitRequestHandler());
            return;
        }
        switch (protocol) {
            case "spdy/3.1":
                pipeline.addLast(new SpdySuitRequestHandler());
            default:
                pipeline.addLast(new HttpSuitRequestHandler());
        }
    }
}
