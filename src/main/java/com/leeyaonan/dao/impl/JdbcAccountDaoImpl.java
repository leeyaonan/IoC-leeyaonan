package com.leeyaonan.dao.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.leeyaonan.dao.AccountDao;
import com.leeyaonan.pojo.Account;
import com.leeyaonan.utils.C3p0Utils;
import com.leeyaonan.utils.ConnectionUtils;
import com.leeyaonan.utils.DruidUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author leeyaonan
 * @Date 2020/4/12 12:23
 */
public class JdbcAccountDaoImpl implements AccountDao {

    private ConnectionUtils connectionUtils;

    public void  setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {
        // 从连接池中获取连接
//        Connection con = DruidUtils.getInstance().getConnection();
        // 2020年4月13日 修改从ConnectionUtils中获取当前线程的连接
        Connection con = connectionUtils.getCurrentThreadConn();


        String sql = "select * from account where cardNo = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, cardNo);
        ResultSet resultSet = preparedStatement.executeQuery();

        Account account = new Account();
        while (resultSet.next()) {
            account.setCardNo(resultSet.getString("cardNo"));
            account.setName(resultSet.getString("name"));
            account.setMoney(resultSet.getInt("money"));
        }

        resultSet.close();
        preparedStatement.close();
        con.close();

        return account;
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {

        // 从连接池中获取连接
//        Connection con = DruidUtils.getInstance().getConnection();
        // 2020年4月13日 修改从ConnectionUtils中获取当前线程的连接
        Connection con = connectionUtils.getCurrentThreadConn();

        String sql = "update account set money = ? where cardNo = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1,account.getMoney());
        preparedStatement.setString(2,account.getCardNo());
        int i = preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
        return i;
    }
}
