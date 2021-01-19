package servlet;
import http.HttpRequest;
import http.HttpResponse;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet{
    public static Logger logger = Logger.getLogger(LoginServlet.class);
    public void service(HttpResponse response, HttpRequest request) {
        logger.info("开始处理登录信息");
        String username = request.getParameter("Username");
        String password = request.getParameter("password");
        if (username == null || password == null) {
            response.setEntity(new File("./webapps/myweb/login_fail.html"));
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile("FILE.dat", "rw");) {
            for (int i = 0; i < raf.length() / 100; i++) {
                raf.seek(100 * 1);
                byte[] data = new byte[32];
                raf.read(data);
                String arr = new String(data, "utf-8").trim();
                System.out.println("第一次读到文件得值为：" + arr);
                if (username.equals(arr)) {
                    raf.read(data);
                    String arr1 = new String(data, "utf-8").trim();
                    System.out.println("第二次读到文件的值：" + arr1);
                    if (password.equals(arr1)) {
                        response.setEntity(new File("./webapps/myweb/login_success.html"));
                        return;
                    }
                    break;
                }
            }
            response.setEntity(new File("./webapps/myweb/login_fail.html"));

        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        logger.info("处理登录信息结束");
    }
}