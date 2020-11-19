package com.github.binarywang.demo.wx.cp.builder;

import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;

import java.sql.SQLException;

public class MyTextCardBuilder {
    public WxCpMessage buildTestCardMsg(String title, String userName, String description, String url, String btn) throws SQLException, ClassNotFoundException {
        return WxCpMessage.TEXTCARD()
        .title(title)
        .description(description)
        .btnTxt(btn)
        .url(url)
        .toUser(userName)
        .build();
    }
}
