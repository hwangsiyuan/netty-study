package com.hussein.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * <p>Title: WebSocketConvertHandler</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/29 2:03 PM
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        switch (msg.getType()) {
            case Binary:
                out.add(new BinaryWebSocketFrame(msg.getData()));
                break;
            case Text:
                out.add(new TextWebSocketFrame(msg.getData()));
                break;
            case Ping:
                out.add(new PingWebSocketFrame(msg.getData()));
                break;
            case Pong:
                out.add(new PongWebSocketFrame(msg.getData()));
                break;
            case Continuation:
                out.add(new ContinuationWebSocketFrame(msg.getData()));
                break;
            case Close:
                out.add(new CloseWebSocketFrame(true, 0, msg.getData()));
                break;
            default:
                throw new IllegalStateException("不支持的webSocket类型" + msg.getType());
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(WebSocketType.Binary, msg.content().copy()));
        } else if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(WebSocketType.Text, msg.content().copy()));
        } else if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(WebSocketType.Ping, msg.content().copy()));
        } else if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(WebSocketType.Pong, msg.content().copy()));
        } else if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(WebSocketType.Close, msg.content().copy()));
        } else if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(WebSocketType.Continuation, msg.content().copy()));
        } else {
            throw new IllegalStateException("不支持的WebSocket" + msg);
        }
    }

    public static final class MyWebSocketFrame {

        private final WebSocketType type;
        private final ByteBuf data;

        public MyWebSocketFrame(WebSocketType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public WebSocketType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }

    public enum WebSocketType {
        Binary,
        Close,
        Continuation,
        Ping,
        Pong,
        Text
    }
}
