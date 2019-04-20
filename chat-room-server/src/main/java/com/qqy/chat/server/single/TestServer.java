package com.qqy.chat.server.single;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 服务器
 * Author:qqy
 */
public class TestServer {
    public static void main(String[] args) {
        try {
            //1.创建服务端的ServerSocket，绑定端口
            ServerSocket serverSocket=new ServerSocket(65521);
            System.out.println("端口号为"+serverSocket.getLocalPort()+"的服务器已启动，等待客户端的连接...");

            //2.接收客户端连接，Socket
            Socket clientSocket=serverSocket.accept();
            System.out.println("接收到客户端"+clientSocket.getRemoteSocketAddress()+"的连接");

            //3.通过Socket发送和接收数据
            //接收的是客户端的OutputStream
            InputStream in=clientSocket.getInputStream();
            //按行读
            Scanner scanner=new Scanner(in);
            System.out.println("接收到客户端的消息："+scanner.nextLine());
            //发送
            OutputStream out=clientSocket.getOutputStream();
            //字符流 -> 字节流
            OutputStreamWriter writer=new OutputStreamWriter(out);
            //切记加上\n，否则客户端无法读取，阻塞
            writer.write("你好，我是服务器\n");
            //一定要刷新
            // 数据写完后，网络传输会将报文分包，到达目的地后再进行组装。
            writer.flush();

            //4.关闭ServerSocket/Socket
            serverSocket.close();
            System.out.println("服务器已关闭...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
