package com.leeyaonan.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author leeyaonan
 * @Date 2020/4/14 1:11
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyResource {
}
