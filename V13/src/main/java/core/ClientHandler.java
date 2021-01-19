package core;
import http.EmptyRequestException;
import http.HttpRequest;
import http.HttpResponse;
import serverlet.LoginServlet;
import serverlet.RegServlet;
import serverlet.UpdatePwdServlet;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
/**

  * 处理与某个客户端的HTTP交互
 * 由于HTTP要求客户端与服务端的交互采取一问一答，因此当前处理流程分为三步：
 * 1：解析请求(读取客户端发送过来的HTTP请求内容)
 * 2：处理请求
 * 3：响应客户端(发送一个HTTP响应给客户端)
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }
    public void run() {
        try {
            //1解析请求
            HttpRequest request=new HttpRequest(socket);
            HttpResponse response=new HttpResponse(socket);
            //2处理请求
            //通过request获取抽象路径
            String path=request.getRequestURI();
            System.out.println(path+"*************************");
            //根据抽象路径去webapps下找到对应的资源
            //首先判断其你去是否为一些特殊的值，用于判定是否为处理业务
            if("/myweb/regUser".equals(path)) {
                RegServlet servlet = new RegServlet();
                servlet.service(request, response);
            }else if("/myweb/loginUser".equals(path)){
                LoginServlet ls = new LoginServlet();
                ls.service(request, response);
            }else if("/myweb/updatePwd".equals(path)){
                UpdatePwdServlet ups = new UpdatePwdServlet();
                ups.service(response,request);
            }else {
                File file = new File("./webapps" + path);
                //检查该资源是否真实存在
                System.out.println("开始执行注册业务");
                if (file.exists() && file.isFile()) {
                    System.out.println("该资源已找到！");
                    //响应该资源
                    response.setEntity(file);
                    //根据正文文件设置响应头
                    //1发送状态行
                    //2发送响应头
                    //3发送响应正文
                } else {
                    System.out.println("该资源不存在！");
                    //响应404
                    File notFoundPage = new File("./webapps/root/404.html");
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                    response.setEntity(notFoundPage);
                    System.out.println("执行注册业务结束");
                }
            }
                response.putHeader("Server", "WebServer");
                //3响应客户端
                response.flush();
         //http://localhost:8088/myweb/hello.html
        } catch (EmptyRequestException e){

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                //响应客户端断开连接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
