package com.github.binarywang.demo.wx.cp.builder;

import me.chanjar.weixin.cp.bean.WxCpMessage;

public class MyTextCardBuilder {
    public WxCpMessage buildTestCardMsg(String title, String userName, String description, String url, String btn) {
        return WxCpMessage.TEXTCARD()
            .title(title)
            .description(description)
            .btnTxt(btn)
            .url(url)
            .toUser(userName)
            .build();
    }
}
