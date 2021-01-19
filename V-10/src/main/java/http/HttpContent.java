package http;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpContent {
    private static Map<String,String> mineMapping = new HashMap<>();
    static{
        initMineMapping();
    }
    private static void initMineMapping(){
        try{
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./config/servlet.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for(Element ele : list){
                String key = ele.element("extension").getText();
                String value = ele.element("mime-type").getText();
                mineMapping.put(key,value);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getMineType(String ele){
        return mineMapping.get(ele);
    }
    public static void main(String[] args){
        System.out.println(mineMapping.size());
    }
}
