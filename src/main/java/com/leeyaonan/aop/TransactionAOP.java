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

import java.lang.reflect.Method;

/**
 * @Author leeyaonan
 * @Date 2020/4/14 9:01
 */
@Aspect
@MyService
public class TransactionAOP {

    @MyAutowired
    TransactionUtils transactionUtils;

    private ProceedingJoinPoint proceedingJoinPoint;

    @Around(value = "execution(* cn.tuhu.springaop.service.impl.*.*(..))")
    public void transactionHandler() throws Throwable {
        transactionHandler();
    }

    @Around(value = "execution(* cn.tuhu.springaop.service.impl.*.*(..))")
    public void transactionHandler(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        TransactionStatus transactionStatus = null;

        if (hasTransaction(proceedingJoinPoint)) {
            transactionStatus = transactionUtils.begin();
        }

        proceedingJoinPoint.proceed();

        // 若hasTransaction(proceedingJoinPoint)判断通过，则transactionStatus不为null
        if (transactionStatus != null) {
            transactionUtils.commit(transactionStatus);
        }
    }

    /**
     * 判断切入点是否标注了@MyTransactional注解
     *
     * @param proceedingJoinPoint
     * </a><a href="/profile/547241" data-card-uid="547241" class="js-nc-card" target="_blank">@return
     */
    private boolean hasTransaction(ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
        this.proceedingJoinPoint = proceedingJoinPoint;
        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        //获取方法所在类的class对象
        Class clazz = proceedingJoinPoint.getSignature().getDeclaringType();
        //获取参数列表类型
        Class[] parameterTypes = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterTypes();
        //根据方法名和方法参列各参数类型可定位类中唯一方法
        Method method = clazz.getMethod(methodName, parameterTypes);
        //根据方法对象获取方法上的注解信息
        MyTransactional myTransactional = method.getAnnotation(MyTransactional.class);
        return myTransactional == null ? false : true;
    }

    @AfterThrowing(value = "execution(* com.leeyaonan.service.impl.*.*(..))")
    public void handleTransactionRollback() throws NoSuchMethodException {
        if (hasTransaction(proceedingJoinPoint)) {
            //获取当前事务并回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
