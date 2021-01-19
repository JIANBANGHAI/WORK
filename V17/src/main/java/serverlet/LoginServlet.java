package serverlet;

import http.HttpRequest;
import http.HttpResponse;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet{
    public static Logger logger = Logger.getLogger(LoginServlet.class);
    public void service(HttpRequest request, HttpResponse response){
        logger.info("开始处理用户登录信息");
        String name = request.getParameter("Username");
        String password = request.getParameter("password");
        if(name == null || password==null){
            response.setEntity(new File("./webapps/myweb/login_fail.html"));
        }
        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
         ){
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(100*i);
                byte[] data = new byte[32];
                raf.read(data);
                String demo = new String(data,"utf-8").trim();
                System.out.println("demo================================================"+demo);
                if(demo.equals(name)){
                    raf.read(data);
                    String pws = new String(data,"utf-8").trim();
                    if(pws.equals(password)){
                        File file1 = new File("./webapps/myweb/login_success.html");
                        response.setEntity(file1);
                        return;
                    }
                    break;
                }
            }
            File file1 = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file1);
        }  catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        logger.info("处理用户登录信息完毕");
    }
}
/*
else {
                    File file = new File("./webapps/login/login_success.html");
                    response.setEntity(file);
                    return;
                }
 */