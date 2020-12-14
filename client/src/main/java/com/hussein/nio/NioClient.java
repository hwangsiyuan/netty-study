package com.hussein.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * <p>Title: NioClient</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/6 5:51 PM
 */
public class NioClient {


    public static void main(String[] args) throws IOException {
        System.out.println("客户端启动");
        new NioClient().start();
    }

    private void start() throws IOException {
        SocketChannel sc = SocketChannel.open();
        try {
            sc.connect(new InetSocketAddress("127.0.0.1", 8070));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner scanner = new Scanner(System.in);
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            new Thread(() -> {
                try {
                    while (true) {
                        readBuffer.clear();
                        sc.read(readBuffer);
                        readBuffer.flip();
                        Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
                        System.out.println("服务端说:" + charset.decode(readBuffer));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            while (true) {
                String input;
                if ((input = scanner.nextLine()) != null) {
                    buffer.clear();
                    buffer.put(input.getBytes(StandardCharsets.UTF_8));
                    buffer.flip();
                    sc.write(buffer);
                }
            }
        } finally {
            sc.close();
        }
    }
}
