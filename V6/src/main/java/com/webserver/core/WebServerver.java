package com.webserver.core;
import com.webserver.core.ClientHandler;
import com.webserver.core.WebServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 小鸟WebServer
 * 模拟Tomcat的基础功能,实现一个简易版的Web容器
 * 基于TCP协议作为通讯协议,使用HTTP协议与客户端进行交互,完成一系列网络操作
 */
public class WebServerver {
    private ServerSocket server;
    public  WebServerver(){
        try {
            System.out.println("正在启动服务端...");
            server = new ServerSocket(8088);
            System.out.println("服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        try {
            System.out.println("等待客户端连接...");
            Socket socket = server.accept();
            System.out.println("一个客户端连接了!");
            //启动一个线程处理客户端交互
            ClientHandler ch = new ClientHandler(socket);
            Thread t = new Thread(ch);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}