package http;

import com.webserver.http.EmptyRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String uri;
    private String protocol;
    private Socket socket;
    private String requestURI;
    private String queryString;
    private Map<String,String> parameters = new HashMap<>();
    private Map<String,String> headers = new HashMap<>();
    private void parseUri(){
        System.out.println("进一步解析uri：");
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
        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
        System.out.println("进一步解析uri结束");
    }
    public HttpRequest(Socket socket) throws EmptyRequestException {
        this.socket = socket;
        System.out.println("开始解析请求...");
        parseRequestLine();
        parseHeaders();
        parseContent();
        parseUri();
        System.out.println("解析请求完毕");
    }
    public void parseRequestLine() throws EmptyRequestException {
        System.out.println("开始解析请求行");
        try {
            String line = readLine();
            if(line.isEmpty()){
                throw new EmptyRequestException();
            }
            System.out.println("请求行" + line);
            String[] data = line.split("\\s");
            method= data[0];
            uri = data[1];
            protocol = data[2];
            System.out.println("a1=" + method);
            System.out.println("a2=" + uri);
            System.out.println("a3=" + protocol);
            HashMap header = new HashMap();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("解析请求行结束");
    }
    public void parseHeaders(){
        System.out.println("正在解析消息头！");
        try {
            while (true){
                String line = readLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] arr = line.split(":\\s");
                headers.put(arr[0],arr[1]);
                System.out.println("消息头："+line);
        }
            System.out.println("headers="+headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("消息头解析完毕");
    }
    public void parseContent(){}
    public String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        StringBuilder  builder = new StringBuilder();
        char a1 = 'a';
        char a2 = 'a';
        int d = -1;
        while ((d = in.read()) != -1){
            a1  =(char)d;
            if(a1 == 10&&a2 == 13){
                break;
            }
            builder.append(a1);
            a2 = a1;
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getParameter(String username) {
        return parameters.get(username);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}