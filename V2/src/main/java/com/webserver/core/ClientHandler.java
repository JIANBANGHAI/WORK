package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
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
            String line = readLine();
            System.out.println("请求行："+line);

            String method;//请求行中的请求方法
            String url;//请求行中的抽象路径
            String protocol;//请求行中的协议版本

            String[] data = line.split("\\s");
            method = data[0];
            url = data[1];
            protocol = data[2];
            System.out.println("method:"+method);
            System.out.println("url:"+url);
            System.out.println("protocol:"+protocol);
            //读取请求头
            //创建一个什么结构保存消息头
            Map<String, String> headers = new HashMap<>();
            while (true) {
                line = readLine();
                if(line.isEmpty()){
                    break;
                }
                String[] arr = line.split(":\\s");
                headers.put(arr[0],arr[1]);
                System.out.println("消息头："+line);
            }
            System.out.println("headers:"+headers);
            //http://localhost:8088/web/hello.html
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readLine() throws IOException {
        //通过socket获取输入流,用于读取客户端发送过来的消息
        InputStream in = socket.getInputStream();
          /*
        socket相同时，无论调用多少次getInputStream()方法。获取的输出流始终是同一个
         */
//                    int d;
//            while ((d = in.read()) != -1) {
//                System.out.print((char) d);
//            }
        //测试读取一行字符串的操作(修改1)
        StringBuilder sb = new StringBuilder();
        char cur = 'a';//本次读到的字符
        char pre = 'a';//上次读到的字符
        int d;
        while ((d = in.read()) != -1) {
            cur = (char) d;
            if (cur == 10 && pre == 13) {
                break;
            }
            sb.append(cur);
            pre = cur;
        }
        return sb.toString().trim();
    }
}