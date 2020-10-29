package com.github.binarywang.demo.wx.cp.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Long> getNewArticleID(Long old) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select articleID from article  where articleID > ?");
        // 4、定义操作的SQL语句
        stmt.setLong(1, old);
        ArrayList<Long> r = new ArrayList<>();
        rs = stmt.executeQuery();
        while (rs.next()){
            r.add(rs.getLong("articleID"));
        }
        return r;
    }

    public static String getTitle(long articleID) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select title from article where articleID = ?");
        // 4、定义操作的SQL语句
        stmt.setLong(1, articleID);
        rs = stmt.executeQuery();
        rs.next();
        return rs.getString("title");
    }

    public static String getContent(long articleID) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select content from article where articleID = ?");
        // 4、定义操作的SQL语句
        stmt.setLong(1, articleID);
        rs = stmt.executeQuery();
        rs.next();
        return rs.getString("content");
    }

    public static List<String> getSubscribers(String tag) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select user_name from subscriber where tag = ?");
        // 4、定义操作的SQL语句
        stmt.setString(1, tag);
        ArrayList<String> r = new ArrayList<>();
        rs = stmt.executeQuery();
        while(rs.next()){
            r.add(rs.getString("user_name"));
        }
        return r;
    }

    public static List<String> getTags(Long articleID) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select tag from tag where articleID = ?");
        // 4、定义操作的SQL语句
        stmt.setLong(1, articleID);
        ArrayList<String> r = new ArrayList<>();
        rs = stmt.executeQuery();
        while(rs.next()){
            r.add(rs.getString("tag"));
        }
        return r;
    }

    public static List<Long> getArticleIDByTag(String tag) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select articleID,tag from tag where tag = ?");
        // 4、定义操作的SQL语句
        stmt.setString(1, tag);
        ArrayList<Long> r = new ArrayList<>();
        rs = stmt.executeQuery();
        while(rs.next()){
            r.add(rs.getLong("articleID"));
        }
        return r;
    }

    public static List<Long> getArticleIDByTitle(String title) throws ClassNotFoundException, SQLException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("select articleID from article where title = ?");
        // 4、定义操作的SQL语句
        stmt.setString(1, title);
        ArrayList<Long> r = new ArrayList<>();
        while(rs.next()){
            r.add(rs.getLong("articleID"));
        }
        return r;
    }

    public static void subscribe(String user_name, String tag) throws SQLException, ClassNotFoundException {
        // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
        Class.forName(driver);
        // 2、获取数据库连接
        conn = DriverManager.getConnection(url, username, password);
        // 3、获取数据库操作对象
        stmt = conn.prepareStatement("insert into subscriber values(?, ?)");
        stmt.setString(1, user_name);
        stmt.setString(2, tag);
        stmt.executeUpdate();

    }
}
