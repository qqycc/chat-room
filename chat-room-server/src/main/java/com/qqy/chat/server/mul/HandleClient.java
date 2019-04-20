package com.qqy.chat.server.mul;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author:qqy
 */
public class HandleClient implements Runnable {
    private static final Map<String, Socket> ONLINE_CLIENT = new ConcurrentHashMap<>();

    private final Socket client;

    public HandleClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //获取客户端的输入
            InputStream in = client.getInputStream();
            //将字符流转换为字节流
            Scanner scanner = new Scanner(in);

            while (true) {
                String data = scanner.nextLine();
                if (data.startsWith("register:")) {
                    if (data.split(":").length == 2) {
                        String userName = data.split(":")[1];
                        //若当前用户存在
                        if (ONLINE_CLIENT.containsKey(userName)) {
                            sendMsg(this.client, "该用户名已存在，请重新注册！！！", false);
                        } else{
                            register(userName);
                        }
                    } else {
                        sendMsg(this.client, "请输入需要注册的用户名！！！", false);
                    }
                    continue;
                }
                if (data.startsWith("groupChat:")) {
                    String msg = data.substring(10);
                    groupChat(msg);
                    continue;
                }
                if (data.startsWith("privateChat:")) {
                    String[] require = data.split(":");
                    String object = require[1];
                    String msg = data.substring(12);
                    privateChat(object, msg);
                    continue;
                }
                if (data.equals("bye")) {
                    bye();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端退出
     */
    private void bye() {
        for (Map.Entry<String, Socket> entry : ONLINE_CLIENT.entrySet()) {
            Socket target = entry.getValue();
            if (target == this.client) {
                ONLINE_CLIENT.remove(entry.getKey());
                break;
            }
        }
        printOnline();
    }

    /**
     * 打印当前在线人数
     */
    private void printOnline() {
        System.out.println("当前在线人数：" + ONLINE_CLIENT.size());
        System.out.println("在线用户为：");
        for (String userName : ONLINE_CLIENT.keySet()) {
            System.out.println(userName);
        }
    }

    /**
     * 私聊
     *
     * @param object 私聊对象
     * @param msg    发送信息
     */
    private void privateChat(String object, String msg) {
        Socket target = ONLINE_CLIENT.get(object);
        if (target == null) {
            sendMsg(this.client, "用户" + object + "不存在！", false);
        } else if(target == this.client){
            sendMsg(this.client, "不能给自己发消息", false);
        }else{
            sendMsg(target, msg, true);
        }
    }

    /**
     * 群聊
     *
     * @param msg 发送信息
     */
    private void groupChat(String msg) {
        for (Map.Entry<String, Socket> entry : ONLINE_CLIENT.entrySet()) {
            Socket target = entry.getValue();
            if (target != this.client) {
                sendMsg(target, msg+"(来自群聊)", true);
            }
        }
    }

    /**
     * 用户注册
     *
     * @param userName 用户名
     */
    private void register(String userName) {
        //TODO 用户名相同如何处理
        ONLINE_CLIENT.put(userName, this.client);
        printOnline();
        sendMsg(this.client, "恭喜" + userName + "，注册成功", false);
    }

    /**
     * 获取用户名
     *
     * @return
     */
    private String getUserName() {
        for (Map.Entry<String, Socket> entry : ONLINE_CLIENT.entrySet()) {
            Socket target = entry.getValue();
            if (target == this.client) {
                return entry.getKey();
            }
        }
        return "";
    }

    private void sendMsg(Socket target, String msg, boolean flag) {
        try {
            OutputStream out = target.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            if (flag) {
                writer.write("<" + getUserName() + "说>" + "\t" + msg + "\n");
            } else {
                writer.write(msg + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
