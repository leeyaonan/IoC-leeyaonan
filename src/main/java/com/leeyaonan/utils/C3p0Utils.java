package com.leeyaonan.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.jboss.C3P0PooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @Author leeyaonan
 * @Date 2020/4/12 15:24
 */
public class C3p0Utils {

    private C3p0Utils() {

    }

    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    static {
        try {
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/rotli");
            dataSource.setUser("root");
            dataSource.setPassword("123456");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static ComboPooledDataSource getInstance() {
        return dataSource;
    }
}
