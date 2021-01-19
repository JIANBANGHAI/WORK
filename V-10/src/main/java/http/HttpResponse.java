package http;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class HttpResponse {
    private Socket socket;
    private int statusCode = 200;
    private String statusReason = "OK";
    private File entity;
    private byte[] data;
    private Map<String,String> headers = new HashMap<>();
    public HttpResponse(Socket socket) {
        this.socket = socket;
    }
    public void flush() {
        System.out.println("开始发送响应！");
        sendRequestLine();
        sendHeaders();
        sendContents();
        System.out.println("发送响应完毕");
    }

    public void sendRequestLine() {
        //1
        System.out.println("开始发送状态行！");
        try {
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            System.out.println("状态行："+line);
            System.out.println("发送状态行结束");
            println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendHeaders() {
        System.out.println("开始发送响应头！");
        try {
            Set<Map.Entry<String,String>> entrySet = headers.entrySet();
            for(Map.Entry<String,String> e: entrySet){
                String key = e.getKey();
                String value = e.getValue();
                String demo = key+": "+value;
                System.out.println("响应头："+demo);
                println(demo);
            }
            println("");
            System.out.println("发送响应头结束");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendContents() {
        System.out.println("开始发送响应正文！");
        if(data!=null){
            OutputStream out = null;
            try {
                out = socket.getOutputStream();
                out.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(entity!=null) {
            try {
                OutputStream out = socket.getOutputStream();
                FileInputStream fis = new FileInputStream(entity);
                int len;
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("发送响应中文结束");
    }
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(line.getBytes("ISO8859-1"));
        out.write(13);
        out.write(10);
    }
    public void putHeader(String key,String value){
        headers.put(key,value);
    }
    public int getStatusCode() {
        return statusCode;
    }
    public String getStatusReason() {
        return statusReason;
    }
    public File getEntity() {
        return entity;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setEntity(File entity) {
        this.entity = entity;
        String ext = entity.getName().substring(entity.getName().lastIndexOf(".")).trim();
        String type = HttpContent.getMineType(ext);
        putHeader("Content-Type",type);
        putHeader("Content-Length",entity.length()+"");
    }

    public void setData(byte[] data) {
        this.data = data;
        putHeader("Content-Length",data.length+"");
    }
}