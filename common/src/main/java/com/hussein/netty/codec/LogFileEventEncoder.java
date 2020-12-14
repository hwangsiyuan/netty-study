package com.hussein.netty.codec;

import com.hussein.netty.model.LogFileEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * <p>Title: LogFileEventEncoder</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/9 6:20 PM
 */
public class LogFileEventEncoder extends MessageToMessageEncoder<LogFileEvent> {

    private final InetSocketAddress remoteAddress;

    public LogFileEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogFileEvent msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(msg.getFileName().getBytes(CharsetUtil.UTF_8));
        buffer.writeBytes(LogFileEvent.SEPARATOR.getBytes(CharsetUtil.UTF_8));
        buffer.writeBytes(msg.getMsg().getBytes(CharsetUtil.UTF_8));
        out.add(new DatagramPacket(buffer, remoteAddress));
    }
}
