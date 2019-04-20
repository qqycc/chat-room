package com.qqy.chat.server.mul;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器
 * Author:qqy
 */
public class MulServer {
    public static void main(String[] args) {
        try {
            int port=65521;
            if(args.length==1){
                try{
                port=Integer.parseInt(args[0]);
                }catch (NumberFormatException e){
                    System.out.println("指定端口号格式错误，采用默认端口号"+port);
                    port=65521;
                }
            }
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("端口号为" + serverSocket.getLocalPort() + "服务器已创建，等待客户端的连接...");

            //创建线程池
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

            //利用循环实现多线程，以支持多用户
            while (true) {
                //客户端连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("接收到客户端"+clientSocket.getRemoteSocketAddress()+"的连接");
                /*
                   不在循环中直接进行业务处理
                   ∵accept()是阻塞方法，若在循环中直接处理业务，每次循环不能很快结束，阻塞了其他客户端的连接
                 */

                //线程池分配线程
                //每次有客户端连接到服务器的时候，就创建一个HandleClient的实例化对象来处理具体的业务
                executorService.execute(new HandleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
