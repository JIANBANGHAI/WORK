package com.webserver.core;

import com.webserver.http.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理与某个客户端的HTTP交互
 * 由于HTTP要求客户端与服务端的交互采取一问一答，因此当前处理流程分为三步：
 * 1：解析请求（读取客户端发送过来的HTTP请求内容）
 * 2：处理请求
 * 3：响应客户端（发送一个HTTP响应给客户端）
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try {
            //1解析请求
            HttpRequest request = new HttpRequest(socket);
            //2处理请求
            //3响应客户端
            File file = new File("./webapps/myweb/index.html");
            OutputStream out = socket.getOutputStream();
            System.out.println("开始发送响应");
            //1发送状态行
            System.out.println("开始发送状态行");
            String line = "HTTP/1.1 200 OK";
            out.write(line.getBytes("ISO8859-1"));
            out.write(10);//发送一个回车键
            out.write(13);//发送一个换行符
            System.out.println("发送状态行完毕");

            System.out.println("开始发送响应头");
            //2发送响应头
             line = "Content-Type: text/html";
             out.write(line.getBytes("ISO8859-1"));
             out.write(13);
             out.write(10);
             //单独发送CRLF表示响应头发送完毕
            line = "Contents-length: "+file.length();
             out.write(13);
             out.write(10);
             out.write(13);
             out.write(10);
            System.out.println("响应头发送完毕");

            //3发送响应行
            System.out.println("开始发送响应行");
            FileInputStream fis = new FileInputStream(file);
            System.out.println("查看异常："+file.length());
            int len;
            byte[] data = new byte[1024*10];
            while ((len = fis.read(data)) !=-1){
                out.write(data,0,len);
            }
            System.out.println("发送响应行完毕");
            System.out.println("发送响应完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}