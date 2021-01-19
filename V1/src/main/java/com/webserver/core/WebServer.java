package com.webserver.core;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 小鸟WebServer
 * 模拟Tomcat的基础功能，实现一个简易版的Web容器
 * 给予TCP协议作为通讯录，使用HTML协议与客户端进行交互，完成一系列网络操作
 */
public class WebServer {
    private ServerSocket server;
    public WebServer(){
        try{
            System.out.println("正在启动服务器");
            server = new ServerSocket(8088);
            System.out.println("服务器启动·完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        try {
            System.out.println("等待客户端连接");
            Socket socket = server.accept();
            System.out.println("一个客户端连接了！");
            //通过socket获取输入流，用于读取用户发送过来的消息
            InputStream in = socket.getInputStream();
            int d;
            while ((d = in.read()) != -1){
                System.out.print((char)d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        WebServer web= new WebServer();
        web.start();
    }
}
