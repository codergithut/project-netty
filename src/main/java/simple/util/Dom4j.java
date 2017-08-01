package simple.util;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.api.scripting.JSObject;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Created by lenovo on 2017/7/26.
 */
public class Dom4j {
    public static Object parse(Element root) {
        List<?> elements = root.elements();
        if (elements.size() == 0) {
            // 没有子元素
            return root.getTextTrim();
        } else {
            // 有子元素
            String prev = null;
            boolean guess = true; // 默认按照数组处理

            Iterator<?> iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element elem = (Element) iterator.next();
                String name = elem.getName();
                if (prev == null) {
                    prev = name;
                } else {
                    guess = name.equals(prev);
                    break;
                }
            }
            iterator = elements.iterator();
            if (guess) {
                List<Object> data = new ArrayList<Object>();
                while (iterator.hasNext()) {
                    Element elem = (Element) iterator.next();
                    ((List<Object>) data).add(parse(elem));
                }
                return data;
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                while (iterator.hasNext()) {
                    Element elem = (Element) iterator.next();
                    ((Map<String, Object>) data).put(elem.getName(), parse(elem));
                }
                return data;
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File("D:\\Biz320802161117000625.xml"));

        Element root = document.getRootElement();

        Object obj = parse(root); // 返回类型未知，已知DOM结构的时候可以强制转换



        System.out.println(JSONObject.toJSONString(obj)); // 打印JSON
    }
}
