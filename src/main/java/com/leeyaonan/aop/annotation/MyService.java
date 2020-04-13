package com.leeyaonan.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解@Service，向容器注入bean
 * 基本思想：
 *      1. 使用Java反射机制扫描包，获取包下所有的类
 *      2. 判断类上是否有注入bean的注解
 *      3. 使用Java的反射机制进行初始化
 * @Author leeyaonan
 * @Date 2020/4/13 13:47
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyService {

}
