package cn.lezizijiang.wechat.cp.suda.noti.cp.builder;

import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class TextBuilder extends AbstractBuilder {

    @Override
    public WxCpXmlOutMessage build(String content, WxCpXmlMessage wxMessage,
                                   WxCpService service) {
        return WxCpXmlOutMessage.TEXT().content(content)
            .fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
            .build();
    }

}
