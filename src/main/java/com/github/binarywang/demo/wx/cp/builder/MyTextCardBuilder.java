package com.github.binarywang.demo.wx.cp.builder;

import me.chanjar.weixin.cp.bean.WxCpMessage;

public class MyTextCardBuilder {
    public WxCpMessage buildTestCardMsg(String title, String userName, String description){
        return WxCpMessage.TEXTCARD()
        .title(title)
        .description(description)
        .btnTxt("更多")
        .toUser(userName)
        .build();
    }
}
