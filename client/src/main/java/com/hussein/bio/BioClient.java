package com.hussein.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * <p>Title: BioClient</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/5 7:47 PM
 */
public class BioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8070);
        try {
            System.out.println("客户端启动..");
            System.out.println("请输入昵称");
            Scanner scanner = new Scanner(System.in);
            String nickname = scanner.nextLine();
            socket.getOutputStream().write((nickname + "\n").getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(serverReader.readLine());
            new Thread(() -> {
                while (true) {
                    BufferedReader sr = null;
                    try {
                        sr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String word;
                        if ((word = sr.readLine()) != null) {
                            System.out.println(word);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            while (true) {
                String input;
                if ((input = scanner.nextLine()) != null) {
                    System.out.println(nickname + ":" + input);
                    socket.getOutputStream().write((input + "\n").getBytes(StandardCharsets.UTF_8));
                    socket.getOutputStream().flush();
                }
            }
        } finally {
            socket.close();
        }
    }
}
