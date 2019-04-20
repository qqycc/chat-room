package com.qqy.chat.client.mul;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端从服务端读取数据的线程
 * Author:qqy
 */
public class ReadDataFromServer extends Thread {
    private Socket clientSocket;

    public ReadDataFromServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            InputStream in=clientSocket.getInputStream();
            Scanner scanner=new Scanner(in);
            while (true){
                System.out.println("来自服务器的消息:\n\t"+scanner.nextLine());
                System.out.println("\n请输入需求——————  ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
