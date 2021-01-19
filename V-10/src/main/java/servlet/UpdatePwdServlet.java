package servlet;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class UpdatePwdServlet extends  HttpServlet{
    public void service(HttpResponse response, HttpRequest request){
        String username = request.getParameter("username");
        String oldPassword = request.getParameter("password");
        String newPassword = request.getParameter("password1");;
        if(username==null || oldPassword ==null){
            response.setEntity(new File("./webapps/myweb/noword.html"));
        }
        try(
                RandomAccessFile raf = new RandomAccessFile("FILE.dat","rw");
            ){
            byte[] data = new byte[32];
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                raf.read(data);
                String name = new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    raf.read(data);
                    String pwd = new String(data,"UTF-8").trim();
                    if(pwd.equals(oldPassword)){
                        //修改新密码
                        raf.seek(i*100+32);
                        data = newPassword.getBytes("UTF-8");
                        data = Arrays.copyOf(data,32);
                        raf.write(data);

                        response.setEntity(new File("./webapps/myweb/okword.html"));
                        return;
                    }
                    break;
                }
            }
            response.setEntity(new File("./webapps/myweb/noword.html"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
