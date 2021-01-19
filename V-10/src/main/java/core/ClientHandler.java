package core;

import com.webserver.http.EmptyRequestException;
import http.HttpRequest;
import http.HttpResponse;
import servlet.*;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try {
            //解析请求
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            //处理请求
            //响应客户端
            String path = request.getRequestURI();
            System.out.println("path=:"+path);
            HttpServlet servlet = ServletContext.getServlet(path);
            if(servlet!=null) {
                servlet.service(response,request);
            }else {
                File file = new File("./webapps" + path);
                if (file.exists() && file.isFile()) {
                    System.out.println("该资源已存在");
                    response.setEntity(file);
                } else {
                    System.out.println("资源不存在");
                    File file1 = new File("./webapps/root/404.html");
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                    response.setEntity(file1);
                }
            }
            response.putHeader("Server","WebServer");
            response.flush();
        }catch(EmptyRequestException e){

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
