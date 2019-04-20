package com.qqy.chat.client.mul;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端给服务端发送数据的线程
 * Author:qqy
 */
public class WriteDatatoServer extends Thread {
    private Socket clientSocket;

    public WriteDatatoServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入需求——————  ");
            while (true) {
                String data=scanner.nextLine();
                writer.write(data+"\n");
                writer.flush();
                if(data.equals("bye")){
                    break;
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
