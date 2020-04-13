package com.leeyaonan.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义@Autowired注解
 * 1.遍历所有注入到IOC容器中的Bean对象
 * 2.遍历对象里的所有属性，判断属性是否带有注解@Autowired
 * 3.如果存在注解，获取属性名称，查找bean容器对象 1参数 当前对象 2参数给属性赋值
 * @Author leeyaonan
 * @Date 2020/4/13 13:50
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutowired {

}
