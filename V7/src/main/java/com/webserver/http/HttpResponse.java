package com.webserver.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 响应当前对象
 * 当前类的每一个实例表示给客户端发送的一个HTTP响应
 * 一个响应应当包含三部分：状态行，响应头，。相应正文
 */
import java.io.File;
public class HttpResponse {
    private int statusCode = 200;//状态代码，默认值为200
    private String statusReason = "ok";//状态描述，默认值OK
    //状态行相关信息
    //响应头相关信息
    //响应正文信息
    private Socket socket;
    private File entity;//响应正文对应的实体文件
    public HttpResponse(Socket socket){
        this.socket = socket;
    }
    /*
    将当前响应对象内容以标准的HTTP响应发送给客户端
     */
    public void flash(){
        System.out.println("开始发送响应行");
        //1发送状态行
        sendStatusLine();
        //2发送响应头
        sendHeaders();
        //3发送响应正文
        sendContent();
        System.out.println("发送响应行完毕");
    }
    private void sendStatusLine(){
        try {
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            System.out.println("状态行："+line);
            println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendHeaders(){
        System.out.println("开始发送响应头");
        try {
            String line = "Content-Type: text/html";
            println(line);
            //单独发送CRLF表示响应头发送完毕
            line = "Content-Length: "+entity.length();
            println(line);
            println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("响应头发送完毕");
    }
    private void sendContent(){
        try {
            System.out.println("开始发送响应正文");
            //1发送状态行
            OutputStream out = socket.getOutputStream();
            //3发送响应行
            FileInputStream fis = new FileInputStream(entity);
            int len;
            byte[] dada = new byte[1024*10];
            while ((len = fis.read(dada)) !=-1){
                out.write(dada,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("发送响应正文行完毕");
    }
    private void println(String line)throws IOException{
        OutputStream out = socket.getOutputStream();
        //2发送响应头
        out.write(line.getBytes("ISO8859-1"));
        out.write(13);
        out.write(10);
    }
    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        this.entity = entity;
    }

    public int getStatusCode(int i) {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason(String notFound) {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}
