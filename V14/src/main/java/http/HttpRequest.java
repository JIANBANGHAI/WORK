package http;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
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
    private String requestURI;//抽象路径中的其你去部分uri中“？”左侧的内容
    private String queryString;//抽象路径中的参数部分，uri中“？”右侧的内容
    private Map<String,String> parameters = new HashMap<>();//保存没一组的参数
    private void parseUri(){
        System.out.println("HttpRequest：进一步解析uri...");
        /*
        对于不含有参数的uri而言则不需要做过多的处理，只需要将uri的值直接赋值给requesUri即可
        因为request专门用来保存uri的请求部分，不含有参数而定就是请求部分

        对于含有参数的uri我们进一步拆分
        首先按照“？”将uri拆分为两部分：请求部分和参数部分
        然后将请求部分赋值给属性requestURI
        将参数请求部分赋值给属性requestString
        只有再对queryString进一步拆分出每一组数组：
        首先按照“&”拆分出每个参数，然后每个参数在按照“=”拆分为参数名和参数值
        将参数名作为key，参数值作为value保存到属性parameters这个Map
        中即可
         http://localhost:63342/WebServer/webapps/myweb/reg.html?_ijt=nku2rvp0glj7o6n8a2s3qs9oas
         */
        //先对uri解码，将%xx的16进制所表示的信息解码还原对应的文字
        try {
            uri = URLDecoder.decode(uri,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(uri.contains("?")){
            String[] demo = uri.split("\\?");
            requestURI = demo[0];
            if(demo.length>1){
                queryString = demo[1];
                System.out.println("requestURI="+requestURI);
                System.out.println("queryString="+queryString);
                demo = queryString.split("&");
                for(String para: demo){
                    String[] paras = para.split("=");
                    if(paras.length>1){
                        parameters.put(paras[0],paras[1]);
                    }else {
                        parameters.put(paras[0],null);
                    }
                }
            }
        }else {
            requestURI = uri;
        }
        System.out.println(queryString);
        System.out.println("HttpRequest：进一步解析uri完毕!");
    }
    public HttpRequest(Socket socket) throws EmptyRequestException {
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
    private void parseRequestLine() throws EmptyRequestException {
        System.out.println("HttpRequest：开始解析请求行...");
        try {
            String line=readLine();
            if(line.isEmpty()){//如果是空字符串，说明是空请求。
                throw new EmptyRequestException();
            }
            System.out.println("请求行："+line);
            String[] data=line.split("\\s");
            method=data[0];
            uri=data[1];
            protocol=data[2];
            parseUri();
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

    public String getRequestURI() {
        return requestURI;
    }
    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name){
        return parameters.get(name);
    }
}