package servlet;
import http.HttpRequest;
import http.HttpResponse;
import vo.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ShowAllUserServlet extends  HttpServlet{
    public void service(HttpResponse response, HttpRequest request){
        System.out.println("ShowAllUserServlet:开始处理用户列表.....");
        //保存user.dat文件中所有的用户信息
        ArrayList<User> list = new ArrayList<>();
        try(RandomAccessFile raf = new RandomAccessFile("FILE.dat","r")){
            for(int i = 0;i<raf.length()/100;i++){
                byte[] data = new byte[32];
                raf.read(data);
                String username = new String(data,"utf-8").trim();
                raf.read(data);
                String password = new String(data,"utf-8").trim();
                raf.read(data);
                String nickname = new String(data,"utf-8").trim();
                int age = raf.readInt();
                User user = new User(username,password,nickname,age);
                list.add(user);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //将生成好的html代码写入一个文件
//        String html = "<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "    <head>\n" +
//                "        <meta charset=\"UTF-8\">\n" +
//                "        <title>用户列表</title>\n" +
//                "    </head>\n" +
//                "    <body>\n" +
//                "    <center>\n" +
//                "        <h1>用户列表</h1>\n" +
//                "        <table border =\"7\">\n" +
//                "            <tr>\n" +
//                "                <td>用户名</td>\n" +
//                "                <td>密码</td>\n" +
//                "                <td>昵称</td>\n" +
//                "                <td>年龄</td>\n" +
//                "            </tr>  ";
//        for(User user: list) {
//            html += "<tr>\n" +
//                    "                <td>" +user.getUsername()+ "</td>\n" +
//                    "                <td>"+user.getPassword()+"</td>\n" +
//                    "                <td>"+user.getNickname()+"</td>\n" +
//                    "                <td>"+user.getAge()+"</td>\n" +
//                    "            </tr> ";
//        }
//        html+="        </table>\n" +
//                "    </center>\n" +
//                "    </body>\n" +
//                "</html>";
        Context context = new Context();
        context.setVariable("list",list);
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode("html");
        resolver.setCharacterEncoding("UTF-8");
        TemplateEngine te = new TemplateEngine();
        te.setTemplateResolver(resolver);
        String html = te.process("./webapps/myweb/userList.html",context);
        try{
            byte[] data = html.getBytes("utf-8");
            response.setData(data);
            response.putHeader("Content-Type","text/html");
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("ShowAllUserServlet:处理用户列表结束！");
    }
}
