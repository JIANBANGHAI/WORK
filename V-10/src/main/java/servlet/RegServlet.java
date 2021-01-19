package servlet;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RegServlet extends  HttpServlet{
    public void service( HttpResponse response,HttpRequest request) {
        System.out.println("RehServlet:开始处理用户注册");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String str = request.getParameter("age");
        if(username==null || password==null || nickname==null || !str.matches("[0-9]{1,2}")){
            File file1 = new File("./webapps/myweb/reg_info_error.html");
            response.setEntity(file1);
            return;
        }
        int age = Integer.parseInt(str);
        System.out.println(username + "," + password + "，" + nickname + "," + age);
        try (
                RandomAccessFile raf = new RandomAccessFile("FILE.dat", "rw");
        ) {
            System.out.println("开始判定文件");
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(100*i);
                byte[] data = new byte[32];
                raf.read(data);
                if(username.equals(data)){
                    File file1 = new File("./webapps/myweb/have_user.html");
                    response.setEntity(file1);
                }
            }
            System.out.println("判定文件结束");
            raf.seek(raf.length());
            byte[] data = username.getBytes("utf-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);
            data = password.getBytes("utf-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            data = nickname.getBytes("utf-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            raf.writeInt(age);
            System.out.println("注册完毕");
            File file = new File("./webapps/myweb/reg_success.html");
            response.setEntity(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("RegServlet:处理注册完毕");
    }
}
