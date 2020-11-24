package com.github.binarywang.demo.wx.cp.utils;

import java.sql.*;

public class DbUtils {
    static String driver = "com.mysql.cj.jdbc.Driver";
    // 数据库连接串
    static String url = "jdbc:mysql://182.92.227.90:3306/wechat?characterEncoding=utf8";
    // 用户名
    static String username = "lzzj";
    // 密码
    static String password = "Gyzsq89897188.a";
    static Connection conn = null;
    static PreparedStatement stmt = null;
    static ResultSet rs = null;
    static String sql = "";

    public static long getLastArticleID() throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select articleID from article order by articleID desc limit 1");
        // 4、定义操作的SQL语句
        rs = stmt.executeQuery();
        rs.next();
        return rs.getLong("articleID");
    }


    public static void subscribe(String user_name, String author) throws SQLException, ClassNotFoundException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("insert into subscriber values(?, ?)");
        stmt.setString(1, user_name);
        stmt.setString(2, author);
        stmt.executeUpdate();

    }
}

// todo:　加入author 到 tag
