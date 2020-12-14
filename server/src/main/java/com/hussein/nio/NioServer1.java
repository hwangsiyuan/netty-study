package com.hussein.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Title: NioServer1</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/6 3:33 PM
 */
public class NioServer1 {

    private static final int PORT = 8070;

    private int port;

    public NioServer1(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("服务器启动...");
        new NioServer1(PORT).serve();
    }

    private void serve() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        try {
            ServerSocket serverSocket = ssc.socket();
            serverSocket.bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();
                    try {
                        keyIterator.remove();
                        //客户端连接成功
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel client = serverChannel.accept();
                            System.out.println("Accepted connection from " + client);
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, ByteBuffer.allocate(100));
                        }
                        //客户端可读
                        if (selectionKey.isReadable()) {
                            SocketChannel client = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                            client.read(buffer);
                            buffer.flip();
                            Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
                            System.out.println(charset.decode(buffer));
                        }
                        //客户端可写
                        if (selectionKey.isWritable()) {
                            SocketChannel client = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                            buffer.flip();
                            client.write(buffer);
                            buffer.compact();
                        }
                    } catch (IOException e) {
                        selectionKey.cancel();
                        try {
                            selectionKey.channel().close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            ssc.close();
        }
    }

}
