package com.leeyaonan.utils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @Author leeyaonan
 * @Date 2020/4/12 12:27
 */
public class DruidUtils {

    private DruidUtils(){
    }

    private static DruidDataSource druidDataSource = new DruidDataSource();


    static {
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/rotli");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
    }

    public static DruidDataSource getInstance() {
        /*
         * 注意：此处代码报过异常，ClassNotFoundException，找不到DruidDataSource
         * 原因：使用tomcat做服务器，编译后，没有在WEB-INF下面生成lib文件夹（即，服务器中没有maven依赖对应的字节码文件）
         * 解决：打开Project Structure -> Artifacts -> Web exploded -> Output Layout -> 右边项目名右键 -> Put into Output Root
         * */
        return druidDataSource;
    }

}
