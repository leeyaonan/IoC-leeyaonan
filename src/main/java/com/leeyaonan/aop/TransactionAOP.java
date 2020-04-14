package com.leeyaonan.aop;

import com.leeyaonan.aop.annotation.MyAutowired;
import com.leeyaonan.aop.annotation.MyService;
import com.leeyaonan.aop.annotation.MyTransactional;
import com.leeyaonan.utils.TransactionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Author leeyaonan
 * @Date 2020/4/14 9:01
 */
@Aspect
@MyService
public class TransactionAOP {

    @MyAutowired
    private TransactionUtils transactionUtils;

    private ProceedingJoinPoint proceedingJoinPoint;

    @Around(value = "execution(* cn.tuhu.springaop.service.impl.*.*(..))")
    public void transactionHandler() throws Throwable {
        transactionHandler();
    }

    // 环绕通知，在方法之前和之后处理事情
    @Around(value = "execution(* cn.tuhu.springaop.service.*.*.*(..))")
    public void transactionHandler(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        TransactionStatus transactionStatus = transactionUtils.begin();

        // 调用目标代理对象方法
        proceedingJoinPoint.proceed();

        this.commit(transactionStatus);
    }

    /**
     * 判断事务的状态，提交事务
     * @param transactionStatus
     */
    private void commit(TransactionStatus transactionStatus) {
        if (null != transactionStatus) {
            System.out.println("--提交事务--");
            transactionUtils.commit(transactionStatus);
        }
    }

    /**
     * 判断是否有@MyTransactional注解，有的话开启事务
     * @param proceedingJoinPoint
     * @return
     */
    private TransactionStatus begin(ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
        // 1. 获取代理对象的方法
        MyTransactional myTransactional = this.getMyTransactional(proceedingJoinPoint);
        TransactionStatus transactionStatus = null;
        if (null != myTransactional) {
            // 开启事务
            System.out.println("--开启事务--");
            transactionStatus = transactionUtils.begin();
        }

        return transactionStatus;
    }

    /**
     * 获取代理对象的方法，判断是否有@MyTransactional注解
     * @param proceedingJoinPoint
     * @return
     */
    private MyTransactional getMyTransactional(ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
        // 获取方法的名称
        String methodName = proceedingJoinPoint.getSignature().getName();
        // 获取目标对象
        Class<?> classTarget = proceedingJoinPoint.getTarget().getClass();
        // 获取目标对象类型
        Class<?>[] par = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterTypes();
        // 获取目标对象方法
        Method objMethod = classTarget.getMethod(methodName, par);
        MyTransactional myTransactional = objMethod.getDeclaredAnnotation(MyTransactional.class);

        return myTransactional;
    }

    // 异常通知
    @AfterThrowing(value = "execution(* com.leeyaonan.service.*.*.*(..))")
    public void handleTransactionRollback() {
        System.out.println("--发生异常，回滚事务--");
        transactionUtils.rollback();
    }
}
