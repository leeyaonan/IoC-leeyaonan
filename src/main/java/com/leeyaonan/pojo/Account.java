package com.leeyaonan.pojo;

/**
 * @Author leeyaonan
 * @Date 2020/4/12 12:19
 */
public class Account {

    private String cardNo;
    private String name;
    private int money;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "cardNo='" + cardNo + '\'' +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
