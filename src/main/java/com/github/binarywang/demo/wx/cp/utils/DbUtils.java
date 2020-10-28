package com.github.binarywang.demo.wx.cp.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbUtils {
    static String driver = "com.mysql.cj.jdbc.Driver";
    // 数据库连接串
    static String url = "jdbc:mysql://182.92.227.90:3306/wechat";
    // 用户名
    static String username = "lzzj";
    // 密码
    static String password = "Gyzsq89897188.a";
    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    static String sql = "";

    public static long getLastArticleID() throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select articleID from article desc limit 1";
        rs = stmt.executeQuery(sql);
        return rs.getLong(0);
    }

    public static List<Long> getNewArticleID(Long old) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select articleID from article  where articleID > " + old;
        ArrayList<Long> r = new ArrayList<>();
        rs = stmt.executeQuery(sql);
        while (rs.next()){
            r.add(rs.getLong(0));
        }
        return r;
    }

    public static String getTitle(long articleID) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select title from article where articleID = " + articleID;
        rs = stmt.executeQuery(sql);
        return rs.getString(0);
    }

    public static String getContent(long articleID) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select content from article where articleID = " + articleID;
        rs = stmt.executeQuery(sql);
        return rs.getString(0);
    }

    public static List<String> getSubscribers(String tag) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select user_name from subscriber where tag = " + tag;
        ArrayList<String> r = new ArrayList<>();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            r.add(rs.getString(0));
        }
        return r;
    }

    public static List<String> getTags(Long articleID) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select tag from tag where articleID = " + articleID;
        ArrayList<String> r = new ArrayList<>();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            r.add(rs.getString(0));
        }
        return r;
    }

    public static List<Long> getArticleIDByTag(String tag) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select articleID from tag where tag = " + tag;
        ArrayList<Long> r = new ArrayList<>();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            r.add(rs.getLong(0));
        }
        return r;
    }

    public static List<Long> getArticleIDByTitle(String title) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        // 4、定义操作的SQL语句
        sql = "select articleID from article where title = " + title;
        ArrayList<Long> r = new ArrayList<>();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            r.add(rs.getLong(0));
        }
        return r;
    }

    public static void subscribe(String username, String tag) throws SQLException, ClassNotFoundException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.createStatement();
        String sql = "insert into table subscriber values(" + username + "," + " lst[i] " + " )";
        rs = stmt.executeQuery(sql);

    }
}
