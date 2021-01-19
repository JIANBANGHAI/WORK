package serverlet;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class UpdatePwdServlet {
    public void service(HttpResponse response, HttpRequest request){
        System.out.println("UpdatePwdServlet:开始处理修改密码...");
        String username = request.getParameter("username");
        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        System.out.println(username+" "+newPassword+" "+oldPassword);
        if(username==null||newPassword==null||oldPassword==null){
            response.setEntity(new File("./webapps/myweb/noword.html"));
        }

        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
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
        }catch(IOException e){
            e.printStackTrace();
        }



        System.out.println("UpdatePwdServlet:处理修改密码完毕!");
    }
}
