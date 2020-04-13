package com.leeyaonan.config;

/**
 * @author Rot
 * @date 2020/4/13 14:11
 */

import com.leeyaonan.aop.annotation.MyResource;
import com.leeyaonan.aop.annotation.MyService;
import com.leeyaonan.utils.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 纯注解方式实现IoC容器的启动类
 */
public class MyAnnotationConfigApplicationContext {

    private String packageName; // 扫描包的名称

    private HashMap<String,Class<?>> beans = null;  // 单例池

    // 有参构造方法
    public MyAnnotationConfigApplicationContext(String packageName) throws Exception{
        this.packageName = packageName;
        beans = new HashMap<>();
        initBeans();
        initEntryField();
    }

    private void initEntryField() throws Exception {
        // 1.遍历所有的bean容器对象
        for (Map.Entry<String, Class<?>> entry : beans.entrySet()) {
            // 2.判断属性上面是否有加注解@EXTREsource 自动注入
            Object bean = entry.getValue();
            attriAssign(bean);
        }
    }

    private void attriAssign(Object object) throws Exception {
        // 1.使用反射机制,获取当前类的所有属性
        Class<? extends Object> classInfo = object.getClass();
        Field[] declaredFields = classInfo.getDeclaredFields();

        // 2.判断当前类属性是否存在注解
        for (Field field : declaredFields) {
            MyResource extResource = field.getAnnotation(MyResource.class);
            if (extResource != null) {
                // 获取属性名称
                String beanId = field.getName();
                Object bean = getBean(beanId);
                if (bean != null) {
                    // 3.默认使用属性名称，查找bean容器对象 1参数 当前对象 2参数给属性赋值
                    field.setAccessible(true); // 允许访问私有属性
                    field.set(object, bean);
                }

            }
        }
    }

    // 初始化对象
    public void initBeans () throws Exception {
        // 1. 使用java反射机制扫包，获取当前包下所有类
        List<Class<?>> classes = ClassUtils.scanPackages(packageName);
        // 2. 遍历判断类上面是否有注解,返回一个map集合，里面包含了所有带MyService注解的类的信息
        HashMap<String,Class<?>>  annotationClasses = findClassIsHasAnnotation(classes);
        if (annotationClasses == null || annotationClasses.isEmpty()){
            throw new Exception("该包下所有类都没有MyService注解");
        }
    }
    public Object getBean (String beanId) throws Exception {
        if (StringUtils.isEmpty(beanId)){
            throw new Exception("bean Id 不能为空");
        }
        //从spring 容器初始化对像
        Class<?> classInfo = beans.get(beanId);
        if (classInfo == null) {
            throw new Exception("Class not found");
        }
        //2.使用反射机制初始化对象
        return newInstance(classInfo);
    }

    //通过反射解析对象
    private Object newInstance(Class<?> classInfo) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //    Class<?> name = Class.forName(className);
        return classInfo.newInstance();
    }
    /*
     * 参数：通过工具类扫描的改包下所有的类信息
     * 返回值：返回一个map集合，里面包含了，所有带ExtService注解的类的信息
     *
     * */
    public  HashMap<String,Class<?>> findClassIsHasAnnotation (List<Class<?>> classes){
        for (Class<?> clazz : classes) {
            // 判断类上是否有自定义MyService注解
            MyService annotation = clazz.getAnnotation(MyService.class);
            if (null != annotation){
                // 获取当前类名
                String className = clazz.getSimpleName();
                // 将类名首字母变为小写
                String beanId = toLowerCaseFirstOne(className);
                // 如果当前类上有MyService注解，将该类的信息，添加到map集合
                beans.put(beanId, clazz);
            }
        }
        return beans;
    }

    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
