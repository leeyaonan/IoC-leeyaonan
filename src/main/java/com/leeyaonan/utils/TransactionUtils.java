package com.leeyaonan.utils;

import com.leeyaonan.aop.annotation.MyAutowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @Author leeyaonan
 * @Date 2020/4/14 9:02
 */
public class TransactionUtils {

    // 事物管理器
    @MyAutowired
    private PlatformTransactionManager dataSourceTransactionManager;


    public TransactionStatus begin() {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        return transactionStatus;
    }

    public void commit(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.commit(transactionStatus);
    }

    public void rollback(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.rollback(transactionStatus);
    }
}
