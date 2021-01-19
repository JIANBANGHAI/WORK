package core;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import servlet.HttpServlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServletContext {
    private static Map<String,HttpServlet> servletMapping = new HashMap<>();
    static{
        initServletMapping();
    }
    private static void initServletMapping(){
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./config/servlet.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("servlet");
            for(Element e: list){
                String key = e.attributeValue("path");
                String className = e.attributeValue("className");
                Class cls = Class.forName(className);
                HttpServlet value = (HttpServlet) cls.newInstance();
                servletMapping.put(key,value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static HttpServlet getServlet(String path){
        return servletMapping.get(path);
    }

    public static void main(String[] args) {
        System.out.println(servletMapping.size());
    }
}
