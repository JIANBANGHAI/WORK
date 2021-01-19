package serverlet;
import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * 用于处理用户注册业务
 */
public class RegServlet {
    public void service(HttpRequest request, HttpResponse response) {

        System.out.println("RegServlet:开始处理用户注册..");
        //1通过request获取用户在页面上表单中输入的信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        /*
        验证数据，若果上述四项存在null。或者年龄的字符串表示的不是一个整数时，
        直接响应一个错误页面：reg_info_error.html，上面居中显示一行字：注册失败，输入信息有误
        此时不应当执行下面的注册操作
         */
        //
        if(username==null|| password==null || nickname==null || ageStr==null  || !ageStr.matches("[0-9]{1,2}+")) {
            File file = new File("./webapps/root/reg_info_error.html");
            response.setEntity(file);
            return;
        }
        int age = Integer.parseInt(ageStr);
            System.out.println(username + "," + password + "," + nickname + "," + age);
            try (
                    RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");
            ) {
                /*
                判定用户是否为重复用户
                先读取user.dat文件中现有的所有用户名，如果与当前注册用户名一致，则直接响应
                页面have_user.html，提示该用户已存在，请重新注册
                否则才将该用户信息写入文件user.dat中完成注册
                 */
                System.out.println("开始判定文件");
                for(int i=0;i<raf.length()/100;i++){
                    raf.seek(100*i);
                    byte[] data = new byte[32];
                    raf.read(data);
                    String demo = new String(data,"utf-8").trim();
                    System.out.println("demo================================================"+demo);
                    if(username.equals(demo)){
                        File file = new File("./webapps/root/have_user.html");
                        response.setEntity(file);
                        return;
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
                File file = new File("./webapps/root/reg_success.html");
                response.setEntity(file);
        /*
        将信息写入文件user.dat中
        每个用户的信息占用100字节，其中用户名，密码，昵称为字符串，各占用32字节
        年龄为int值占用4字节
         */
                //2将信息写入文件user.dat中

                //3响应客户端注册1结果页面
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
        }
        System.out.println("RegServlet:处理注册完毕");
    }
}
