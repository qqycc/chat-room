package com.qqy.chat.client.mul;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端
 * Author:qqy
 */
public class MulClient {

    public static void main(String[] args) {
        try {
            String host = "127.0.0.1";
            int port = 65521;
            //先读取地址，再读取端口号
            if (args.length == 2) {
                host = args[0];
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("指定端口号格式错误，采用默认端口号" + port);
                    port = 65521;
                }
            }

            Socket clientSocket = new Socket(host, port);
            System.out.println("端口号为" + clientSocket.getLocalPort() + "的客户端已创建");
            System.out.println("已连接上端口号为" + clientSocket.getPort() + "的服务器...");

            System.out.println("\n请按照提示信息进行操作：");
            System.out.println("\t请求按行读取");
            System.out.println("\t注册： register:<userName> 例如： register:lila");
            System.out.println("\t群聊： groupChat:<message> 例如： groupChat:大家好");
            System.out.println("\t私聊： privateChat:<userName>:<message> 例如： privateChat:nina:你好呀");
            System.out.println("\t退出:  bye\n");

            new WriteDatatoServer(clientSocket).start();
            new ReadDataFromServer(clientSocket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
