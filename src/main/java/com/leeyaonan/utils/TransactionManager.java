package com.leeyaonan.utils;

import com.leeyaonan.aop.annotation.MyAutowired;
import com.leeyaonan.aop.annotation.MyService;

import java.sql.SQLException;

/**
 * @Author leeyaonan
 * @Date 2020/4/13 7:58
 */
@MyService(name = "transactionManager")
public class TransactionManager {

    @MyAutowired
    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    // 开启手动事务控制
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }


    // 提交事务
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }


    // 回滚事务
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }

}
