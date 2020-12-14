package com.hussein.netty.handler;

import io.netty.channel.*;

import java.io.File;
import java.io.FileInputStream;

/**
 * <p>Title: FileRegionHandlerInitializer</p>
 * <p>Description: zero-memory-copy</p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/1 2:45 PM
 */
public class FileRegionHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;

    public FileRegionHandlerInitializer(File file) {
        this.file = file;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new FileRegionHandler());
    }

    private final class FileRegionHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            FileInputStream fis = new FileInputStream(file);
            FileRegion fr = new DefaultFileRegion(fis.getChannel(), 0, file.length());
            ctx.channel().writeAndFlush(fr).addListener(
                    (ChannelFutureListener) future -> {
                        if (!future.isSuccess()) {
                            //Do something
                        }
                    }
            );
        }
    }
}
