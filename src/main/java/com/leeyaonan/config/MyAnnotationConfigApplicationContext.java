package com.leeyaonan.config;

/**
 * @author Rot
 * @date 2020/4/13 14:11
 */

import com.leeyaonan.aop.annotation.MyAutowired;
import com.leeyaonan.aop.annotation.MyResource;
import com.leeyaonan.aop.annotation.MyService;
import com.leeyaonan.utils.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 纯注解方式实现IoC容器的启动类
 */
public class MyAnnotationConfigApplicationContext {
    private String path;

    ConcurrentHashMap<String, Object> beans = null;

    public MyAnnotationConfigApplicationContext(String path) {
        this.path = path;
    }


    /**
     * 根据beanid获取对应的bean
     *
     * @param beanId
     * @return
     * @throws Exception
     */
    public Object getBean(String beanId) throws Exception {
        List<Class> classes = findAnnoationService();
        if (classes == null || classes.isEmpty()) {
            throw new Exception("未找到可初始化的类");
        }
        beans = initBean(classes);
        if (beans == null || beans.isEmpty()) {
            throw new Exception("单例池为空");
        }
        Object object = beans.get(beanId);
        //初始化属性的依赖
        initAttribute(object);
        return object;
    }

    /**
     * 初始化依赖的属性
     *
     * @param object
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void initAttribute(Object object) throws Exception {
        //获取object的所有类型
        Class<? extends Object> classinfo = object.getClass();
        //获取所有的属性字段
        Field[] fields = classinfo.getDeclaredFields();
        //遍历所有字段
        for (Field field : fields) {
            //查找字段上有依赖的注解
            boolean falg = field.isAnnotationPresent(MyAutowired.class);
            if (falg) {
                MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
                if (null != myAutowired) {
                    //获取属性的beanid
                    String beanId = field.getName();
                    //获取对应的object
                    Object attrObject = getBean(beanId);
                    if (attrObject != null) {
                        //访问私有字段
                        field.setAccessible(true);
                        //赋值
                        field.set(object, attrObject);
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 初始化bean
     *
     * @param classes
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public ConcurrentHashMap<String, Object> initBean(List<Class> classes) throws IllegalAccessException, InstantiationException {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
        String beanId = "";
        for (Class clazz : classes) {
            Object object = clazz.newInstance();
            MyService annotation = (MyService) clazz.getDeclaredAnnotation(MyService.class);
            if (annotation != null) {
                //如果定义了name属性 以实现的name属性为主否则以默认的规则为主
                String value = annotation.name();
                if (value != null && !value.equals("")) {
                    beanId = value;
                } else {
                    beanId = toLowerCaseFirstOne(clazz.getSimpleName());
                }
            }

            //存储值
            map.put(beanId, object);
        }
        return map;
    }

    /**
     * 查找包路径下面所有添加注解的类 @MyService
     *
     * @return
     * @throws Exception
     */
    private List<Class> findAnnoationService() throws Exception {
        if (path == null || path.equals("")) {
            throw new Exception("scan package address is null or empty..");
        }
        //获取包下面所有的类
        List<Class<?>> classes = ClassUtils.getClasses(path);
        if (classes == null || classes.size() == 0) {
            throw new Exception("not found service is added annoation for @iocservice");
        }
        List<Class> annoationClasses = new ArrayList<Class>();
        for (Class clazz : classes) {
            //通过反射机制 查找增加了注解的类
            MyService myService = (MyService) clazz.getDeclaredAnnotation(MyService.class);
            if (myService != null) {
                annoationClasses.add(clazz);
                continue;
            }
        }
        return annoationClasses;
    }


    /**
     * 首字母转换为小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
