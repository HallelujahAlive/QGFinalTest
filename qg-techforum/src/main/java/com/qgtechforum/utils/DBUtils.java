package com.qgtechforum.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//创建通用工具类，用于连接数据库
public class DBUtils {
    //定义数据库连接的属性:url,username,password
    //都要设置为静态的，因为我们要在其他类中使用这些属性，也要机上final关键字，因为这些属性的值是不会改变的
    private static final String url = "jdbc:mysql://localhost:3306/db2?useSSL=false&serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "123456";
    //定义数据库连接的方法
    public static Connection getConnection(){
        //使用try-catch语句捕获异常
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
