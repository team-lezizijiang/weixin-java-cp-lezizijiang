package com.github.binarywang.demo.wx.cp.handler;

import com.github.binarywang.demo.wx.cp.builder.TextBuilder;
import com.github.binarywang.demo.wx.cp.utils.JsonUtils;
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
        if (msgType == null) {
            // 如果msgType没有，就自己根据具体报文内容做处理
        }

//        if (msgType.equals(WxConsts.XmlMsgType.EVENT)) {
//
//        }
        //TODO 组装回复消息
        String contents = wxMessage.getContent();
        String FromUserName = wxMessage.getFromUserName();
        String driver = "com.mysql.jdbc.Driver";
        // 数据库连接串
        String url = "jdbc:mysql://182.92.227.90:3306/wechat";
        // 用户名
        String username = "lezizijiang";
        // 密码
        String password = "Lzzj0821.a";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String content = null;
        StringBuffer stringBuffer = new StringBuffer("您的关键词查询到的内容有\n");
        try {
            // 1、加载数据库驱动（ 成功加载后，会将Driver类的实例注册到DriverManager类中）
            Class.forName(driver);
            // 2、获取数据库连接
            conn = DriverManager.getConnection(url, username, password);
            // 3、获取数据库操作对象
            stmt = conn.createStatement();
            // 4、定义操作的SQL语句
            String sql = "select b.title from tag a, article b where a.tag =" + contents + " and b.id = a.id";
            // 5、执行数据库操作
            rs = stmt.executeQuery(sql);
            // 6、获取并操作结果集
            while(rs.next()){
                stringBuffer.append(rs.getInt("title\n"));
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
