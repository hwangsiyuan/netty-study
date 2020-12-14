package com.hussein.netty.codec;

import com.hussein.netty.model.LogFileEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * <p>Title: LogFileEventDecoder</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/10 10:12 AM
 */
public class LogFileEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf content = datagramPacket.content();
        String logEventStr = content.toString(CharsetUtil.UTF_8);
        String[] logFileArray = logEventStr.split(LogFileEvent.SEPARATOR);
        out.add(new LogFileEvent(datagramPacket.recipient(), System.currentTimeMillis(), logFileArray[0], logFileArray[1]));
    }
}
