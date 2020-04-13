package com.leeyaonan.service;

/**
 * @Author Leeyaonan
 * @Date 2020/4/12 12:09
 */
public interface TransferService {

    void transfer(String fromCardNo, String toCardNo, int money) throws Exception;
}
