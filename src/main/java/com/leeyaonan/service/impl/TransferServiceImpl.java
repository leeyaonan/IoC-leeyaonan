package com.leeyaonan.service.impl;

import com.leeyaonan.aop.annotation.MyAutowired;
import com.leeyaonan.aop.annotation.MyService;
import com.leeyaonan.aop.annotation.MyTransactional;
import com.leeyaonan.dao.AccountDao;
import com.leeyaonan.dao.impl.JdbcAccountDaoImpl;
import com.leeyaonan.pojo.Account;
import com.leeyaonan.service.TransferService;

/**
 * @Author Leeyaonan
 * @Date 2020/4/12 12:09
 */
@MyService(name = "transferService")
public class TransferServiceImpl implements TransferService {

    @MyAutowired
    private AccountDao accountDao;

    // 构造函数传值/set方法传值

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }



    @Override
    @MyTransactional
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney()-money);
        to.setMoney(to.getMoney()+money);

        accountDao.updateAccountByCardNo(to);
        int c = 1/0;
        accountDao.updateAccountByCardNo(from);

    }
}
