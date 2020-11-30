package com.github.binarywang.demo.wx.cp.builder;

import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;


/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class ImageBuilder extends AbstractBuilder {

    @Override
    public WxCpXmlOutMessage build(String content, WxCpXmlMessage wxMessage,
                                   WxCpService service) {

        return WxCpXmlOutMessage.IMAGE().mediaId(content)
            .fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
            .build();
    }

}
