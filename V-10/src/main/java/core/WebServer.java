package core;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 小鸟WebServer
 * 模拟Tomcat的基础功能,实现一个简易版的Web容器.
 * 基于TCP协议作为通讯协议,使用HTTP协议与客户端进行交互,完成一系列网络操作.
 */
public class WebServer {
    private ServerSocket server;
    private ExecutorService threadPool;
    public WebServer(){
        try{
            System.out.println("正在启动服务器");
            server = new ServerSocket(8088);
            threadPool = Executors.newFixedThreadPool(30);
            System.out.println("服务器启动完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        try {
            while (true) {
                System.out.println("等待客户端连接");
                Socket socket = server.accept();
                System.out.println("一个用户已连接");
                ClientHandler chr = new ClientHandler(socket);
                threadPool.execute(chr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        WebServer web = new WebServer();
        web.start();
    }
}