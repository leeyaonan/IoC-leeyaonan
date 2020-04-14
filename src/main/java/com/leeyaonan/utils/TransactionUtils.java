package com.leeyaonan.utils;

import com.leeyaonan.aop.annotation.MyAutowired;
import com.leeyaonan.aop.annotation.MyService;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @Author leeyaonan
 * @Date 2020/4/14 9:02
 */
@MyService(name = "transactionUtils")
public class TransactionUtils {

    private TransactionStatus transactionStatus;

    // 事物管理器
    @MyAutowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    // 开启事务
    public TransactionStatus begin() {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        return transactionStatus;
    }

    // 提交事务
    public void commit(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.commit(transactionStatus);
    }

    // 回滚事务
    public void rollback() {
        dataSourceTransactionManager.rollback(transactionStatus);
    }
}
