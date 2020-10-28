package com.github.binarywang.demo.wx.cp.handler;

import com.github.binarywang.demo.wx.cp.builder.TextBuilder;
import java.sql.*;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService,
                                    WxSessionManager sessionManager) {
        final String msgType = wxMessage.getMsgType();
        //TODO 组装回复消息
        String contents = wxMessage.getContent();
        String FromUserName = wxMessage.getFromUserName();
        String driver = "com.mysql.cj.jdbc.Driver";
        // 数据库连接串
        String url = "jdbc:mysql://182.92.227.90:3306/wechat";
        // 用户名
        String username = "lzzj";
        // 密码
        String password = "Gyzsq89897188.a";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String content;
        StringBuffer stringBuffer = new StringBuffer();
        String[] lst = contents.split(" ");
        try {
            // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
            Class.forName(driver);
            // 2、获取数据库连接
            conn = DriverManager.getConnection(url, username, password);
            // 3、获取数据库操作对象
            stmt = conn.createStatement();
            // 4、定义操作的SQL语句
            if("#查找关键词".equals(lst[0])){
                if(lst.length == 1){
                    stringBuffer.append("对不起，您输入的格式有误，请按照#查找关键词 关键词1 关键词2....的格式使用，中间需要加上空格");
                }
                for (int i = 1; i < lst.length; i++) {
                    String sql = "select b.title from tag a, article b where a.tag = " + lst[i] + " and b.id = a.id";
                    // 5、执行数据库操作
                    stringBuffer.append("您查询的关键词"+lst[i]+"对应的标题有\n");
                    rs = stmt.executeQuery(sql);
                    if(!rs.next()){

                    }
                    // 6、获取并操作结果集
                    while (rs.next()) {
                        stringBuffer.append(rs.getBigDecimal("title"));
                        stringBuffer.append("\n");
                    }
                    if("".equals(stringBuffer.toString())){
                        stringBuffer.append("您输入的这个关键词尚无记录");
                    }
                }
            }else if("#订阅".equals(lst[0])){
                if(lst.length == 1){
                    stringBuffer.append("对不起，您输入的格式有误，请按照#订阅 关键词1 关键词2....的格式使用，中间需要加上空格");
                }
                for (int i = 1; i < lst.length; i++) {
                    String sql = "insert into table subscriber values("+FromUserName+","+"lst[i]"+")";
                    rs = stmt.executeQuery(sql);
                }
            }else if("#查询文章内容".equals(lst[0])){
                if(lst.length == 1){
                    stringBuffer.append("对不起，您输入的格式有误，请按照#查询文章内容 标题1的格式使用，中间需要加上空格");
                }
                for (int i = 1; i < lst.length; i++) {
                    String sql = "select content from article where title = "+lst[i];
                    rs = stmt.executeQuery(sql);
                }
                if(stringBuffer.toString().equals("")){
                    stringBuffer.append("对不起，您输入的文章标题没有记录");
                }
            }else{
                stringBuffer.append("您输入内容无效，请输入\n#订阅 关键词\n或者#查找关键词 关键词\n或者#查找文章内容 文章标题");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 7、关闭对象，回收数据库资源
        if (rs != null) { //关闭结果集对象
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) { // 关闭数据库操作对象
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) { // 关闭数据库连接对象
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        content = stringBuffer.toString();
        return new TextBuilder().build(content, wxMessage, cpService);
    }

}
