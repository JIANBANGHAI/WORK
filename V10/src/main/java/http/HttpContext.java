package http;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
/**
 * HTTP协议规定所有的内容都定义在这里，以便将来重用
 */
public class HttpContext {
    /**
     * Content-Type 信息
     * key:资源的后缀名
     * VALUE:对应的Content-Type的值
     */
    private static Map<String,String> mineMapping = new HashMap<>();
    static{
        initMineMapping();
    }
    private static void initMineMapping() {
//        mineMapping.put("html","text/html");
//        mineMapping.put("css","text/css");
//        mineMapping.put("js","application/javascript");
//        mineMapping.put("png","image/png");
//        mineMapping.put("jpg","image/jpeg");
//        mineMapping.put("gif","image/gif");
        /*
        通过解析config/web.xml文件初始化mineMapping
        经根标签所有名为<mine-mapping>的字标签获取到
        并将其中的字标签：
        <extension>中间的文本作为key
        <mine-type>中间的文本作为value
        保存到mineMapping这个Map中
        初始化完毕后，mineMapping中应当有1011个元素
         */
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./congig/web.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for(Element e: list){
                String name = e.element("extension").getText();
                String name1 = e.element("mime-type").getText();
                mineMapping.put(name,name1);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
        /**
     * 根据资源的后缀名获取对应的Content-Type的值
     * @param ext
     * @return
     */
    public static String getMinetype(String ext) {
        return mineMapping.get(ext);
    }

    public static void main(String[] args) {
        System.out.println(mineMapping.size());
    }
}
