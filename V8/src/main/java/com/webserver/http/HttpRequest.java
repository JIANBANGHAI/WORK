package src.main.java.com.webserver.http;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
/**
 * 请求对象
 * 该类的每一个实例用于表示浏览器发送过来的一个HTTP请求
 * HTTP协议要求一个请求由三部分构成：
 * 请求行，消息头，消息正文
 */
public class HttpRequest {
    //请求行相关信息

    private String method;//请求行中的请求方式
    private String uri;//请求行中的抽象路径
    private String protocol;//请求行中的协议版本
    //消息头相关信息
    private Map<String, String> headers = new HashMap<>();
    //消息正文相关信息
    private Socket socket;
    public HttpRequest(Socket socket) throws com.webserver.http.EmptyRequestException {
        System.out.println("HttpRequest:开始解析请求...");
        this.socket = socket;
        //1解析请求行
        parseRequestLine();
        //2解析消息头
        parseHeaders();
        //3解析消息正文
        parseContent();
        System.out.println("HttpRequest：请求解析完毕！");
    }
    private void parseRequestLine() throws com.webserver.http.EmptyRequestException {
        System.out.println("HttpRequest：开始解析请求行...");
        try {
            String line=readLine();
            if(line.isEmpty()){//如果是空字符串，说明是空请求。
                throw new com.webserver.http.EmptyRequestException();
            }
            System.out.println("请求行："+line);

            String[] data=line.split("\\s");
            method=data[0];
            uri=data[1];
            protocol=data[2];
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest:请求行解析完毕！");
    }

    private void parseHeaders(){
        System.out.println("HttpRequest:开始解析消息头...");
        try {
            while (true){
               String line=readLine();
                if(line.isEmpty()){
                    break;
                }
                String[] arr=line.split(".\\s");
                headers.put(arr[0],arr[1]);
                System.out.println("消息头："+line);
            }
            System.out.println("headers:"+headers);
            System.out.println("消息头读取完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest:消息头解析完毕!");
    }
    private void parseContent(){
        System.out.println("HttpRequest:开始解析消息正文...");
        System.out.println("HttpRequest:消息正文解析完毕！");
    }

    private String readLine() throws IOException {
    /*
          socket相同时，无论调用多少次getInputStream()方法，获取的输入流始终是同一个
       */
        InputStream in = socket.getInputStream();
        //测试读取一行字符串的操作
        StringBuilder builder = new StringBuilder();
        int d;
        char cur = 'a';//本次读取到的字符
        char pre = 'a';//上次读取到的字符
        while ((d = in.read()) != -1) {
            cur = (char) d;
            if (pre == 13 && cur == 10) {
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }
}