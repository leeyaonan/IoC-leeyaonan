package com.leeyaonan.factory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author leeyaonan
 * @Date 2020/4/12 23:53
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory {

    /*
    * BeanFactory有两个任务
    *   1. 读取和解析beans.xml文件，通过反射技术实例化对象并且存储在map集合中待用
    *   2. 对外提供获取实例对象的接口（根据id获取）
    * */

    private static Map<String, Object> map = new HashMap<>();   // 存储对象

    // 1. 读取和解析beans.xml文件，通过反射技术实例化对象并且存储在map集合中待用
    static {
        // 静态代码块读取解析xml

        // 1. 加载xml配置文件
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("applicationContext.xml");
        // 2. 解析xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> beanList = rootElement.selectNodes("//beans");
            for (int i = 0; i < beanList.size(); i++) {
                Element element = beanList.get(i);
                // 处理每一个bean元素，获取到该元素的id和class属性
                String id = element.attributeValue("id");
                String clazz = element.attributeValue("class");
                // 通过反射技术实例化对象
                Class<?> aClass = Class.forName(clazz);
                Object o = aClass.newInstance(); // Java9之后直接newInstance是过时的方法，推荐使用class.getDeclaredConstructor().newInstance()

                // 存储到map中待用
                map.put(id, o);
            }

            // 实例化完成之后维护对象的依赖关系，检查哪些对象需要传值进入，根据它的配置，传入相应的值
            // 有property子元素就有传值需求
            List<Element> propertyList = rootElement.selectNodes("//property");
            // 解析property，获取父元素
            for (int i = 0; i < propertyList.size(); i++) {
                Element element = propertyList.get(i);
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                // 找到当前需要被处理依赖关系的bean
                Element parent = element.getParent();

                // 调用父元素对象的反射功能
                String parentId = parent.attributeValue("id");
                Object parentObject = map.get(parentId);
                // 遍历父对象中的所有方法，找到“set” + name
                Method[] methods = parentObject.getClass().getMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    if (("set" + name).equalsIgnoreCase(method.getName())) {
                        method.invoke(parentObject, map.get(ref));
                    }
                }

                // 把处理之后的parentObject重新放到map中
                map.put(parentId, parentObject);
            }

        } catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // 2. 对外提供获取实例对象的接口（根据id获取）
    public static Object getBean(String id) {
        return map.get(id);
    }
}
