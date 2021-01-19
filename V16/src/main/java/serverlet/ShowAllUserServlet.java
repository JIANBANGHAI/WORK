package serverlet;

import http.HttpRequest;
import http.HttpResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import vo.User;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
/**
 * 生成包含user.dat文件中所有的用户信息的动态页面
 */
public class ShowAllUserServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("ShowAllUserServlet:开始处理用户列表页面");
        //先将user.dat文件中所有的文件读取出来

        ArrayList<User> list = new ArrayList<>();//保存user.dat文件中所有的用户信息
        try (RandomAccessFile raf = new RandomAccessFile("user.dat","r");){
            for(int i=0;i<raf.length()/100;i++){
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
                System.out.println("ShowAllUserServlet：当前页面user="+user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        //初始化模板解释器，用来告知引擎有关模板页面的相关信息
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode("html");//指定格式
        resolver.setCharacterEncoding("UTF-8");//模板字符集
        //实例化模板引擎
        TemplateEngine te = new TemplateEngine();
        te.setTemplateResolver(resolver);//设置解释器，使其了解模板相关信息
        /*
        String process(String path,Context cts)
        模板引擎生成动态页面的方法
        参数1：模板页面的路径
        参数2：需要在页面上显示的数据(数据应当都放在这个Context中)
        返回值为生成好的html代码
         */
        String html = te.process("./webapps/myweb/userList.html",context);
        //将生成好的html代码写入一个文件
        try{
            byte[] data = html.getBytes("utf-8");
            response.setData(data);
            response.putHeader("Content-Type","text/html");
            //将生成的页面设置到response中
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("ShowAllUserServlet:处理用户列表页面结束！");
    }

}
