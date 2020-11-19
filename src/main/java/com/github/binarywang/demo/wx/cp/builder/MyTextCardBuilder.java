package com.github.binarywang.demo.wx.cp.builder;

import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;

import java.sql.SQLException;

public class MyTextCardBuilder {
    public WxCpMessage buildTestCardMsg(String title, String userName, Long articleID) throws SQLException, ClassNotFoundException {
        return WxCpMessage.TEXTCARD()
        .title(title)
        .description(DbUtils.getTitle(articleID))
        .btnTxt("更多")
        .url("https://message.lezizijiang.cn/content/?articleID=" + articleID)
        .toUser(userName)
        .build();
    }
}
