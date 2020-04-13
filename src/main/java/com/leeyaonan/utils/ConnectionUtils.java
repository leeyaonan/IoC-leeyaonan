package com.leeyaonan.utils;

import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author leeyaonan
 * @Date 2020/4/13 7:52
 */
public class ConnectionUtils {

/*    private ConnectionUtils() {

    }

    private static ConnectionUtils connectionUtils = new ConnectionUtils();

    public static ConnectionUtils getInstance() {
        return connectionUtils;
    }*/

    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();  // 存储当前线程的连接

    public Connection getCurrentThreadConn() throws SQLException {
        // 判断当前线程中是否已经绑定连接，如果没有绑定，需要从连接池中获取一个连接绑定到当前线程
        Connection connection = threadLocal.get();
        if (null == connection) {
            // 从连接池中拿取连接并绑定到线程
            connection = DruidUtils.getInstance().getConnection();
            // 绑定到当前线程
            threadLocal.set(connection);
        }
        return connection;
    }
}
