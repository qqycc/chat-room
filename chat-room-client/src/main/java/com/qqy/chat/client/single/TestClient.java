package com.qqy.chat.client.single;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端
 * Author:qqy
 */
public class TestClient {
    public static void main(String[] args) {
        try {
            //1.创建Socket，指定服务器的地址和服务端口，连接指定服务
            Socket clientSocket=new Socket("127.0.0.1",65521);
            System.out.println("端口号为"+clientSocket.getLocalPort()+"的客户端已创建");
            System.out.println("该客户端已连接上端口号为"+clientSocket.getPort()+"的服务器");

            //2.通过Socket发送和接收数据
            //发送
            OutputStream out=clientSocket.getOutputStream();
            OutputStreamWriter write=new OutputStreamWriter(out);
            write.write("你好，我是客户端\n");
            write.flush();
            //接收的是服务器的OutputStream
            InputStream in =clientSocket.getInputStream();
            Scanner scanner=new Scanner(in);
            System.out.println("接收到服务器的消息："+scanner.nextLine());

            //3.关闭Socket
            clientSocket.close();
            System.out.println("客户端已关闭...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
