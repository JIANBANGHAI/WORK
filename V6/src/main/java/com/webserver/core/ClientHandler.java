package com.webserver.core;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
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
            HttpResponse response = new HttpResponse(socket);
            //2处理请求
            //通过request获取抽象路径
            String path = request.getUrl();
            File file = new File("./webapps"+path);//根据抽象路径·取webapps下找到对应的资源
            if(file.exists() && file.isFile()){
                System.out.println("该资源已找到");
                //响应该资源
                response.setEntity(file);
            }else {//检查该资源是否存在
                System.out.println("该资源不存在");
                File notFoundPage = new File("./webapps/root/404.html");
                response.getStatusCode(404);
                response.getStatusReason("NotFound");
                response.setEntity(notFoundPage);
            }
            response.flash();
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