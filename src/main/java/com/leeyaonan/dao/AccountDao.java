package com.leeyaonan.dao;

import com.leeyaonan.pojo.Account;

import java.sql.SQLException;

/**
 * @Author Leeyaonan
 * @Date 2020/4/12 12:09
 */
public interface AccountDao {
    Account queryAccountByCardNo(String fromCardNo) throws Exception;

    int updateAccountByCardNo(Account from) throws Exception;
}
