package com.hussein.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title: BioServer</p>
 * <p>Description: bio服务</p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/5 5:41 PM
 */
public class BioServer {

    private static final int PORT = 8070;

    private int port;

    private Map<String, Socket> clientMap = new ConcurrentHashMap<>();

    private BioServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        new BioServer(PORT).listening();
    }

    private void listening() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务端启动..");
        try {
            while (true) {
                final Socket client = serverSocket.accept();
                System.out.println("客户端连接..");
                new Thread(() -> {
                    try {
                        final InputStream inputStream = client.getInputStream();
                        String nickname = sayWelcome(client);
                        InputStreamReader ir = new InputStreamReader(inputStream);
                        BufferedReader br = new BufferedReader(ir);
                        String line;
                        while (true) {
                            if ((line = br.readLine()) != null) {
                                sendToOthers(client, nickname, line);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private void sendToOthers(Socket client, String nickname, String line) {
        clientMap.forEach((name, socket) -> {
            if (client != socket) {
                PrintWriter otherPw = null;
                try {
                    otherPw = new PrintWriter(socket.getOutputStream());
                    otherPw.write(nickname + ":" + line + "\n");
                    otherPw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String sayWelcome(Socket client) throws IOException {
        InputStreamReader ir = new InputStreamReader(client.getInputStream());
        BufferedReader br = new BufferedReader(ir);
        String nickname = br.readLine();
        clientMap.put(nickname, client);
        PrintWriter pw = new PrintWriter(client.getOutputStream());
        pw.write("欢迎您加入聊天室" + "\n");
        pw.flush();
        clientMap.forEach((name, socket) -> {
            if (client != socket) {
                PrintWriter otherPw = null;
                try {
                    otherPw = new PrintWriter(socket.getOutputStream());
                    otherPw.write("欢迎" + nickname + "加入聊天室" + "\n");
                    otherPw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return nickname;
    }
}
