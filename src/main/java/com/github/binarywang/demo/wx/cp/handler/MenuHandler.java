package com.github.binarywang.demo.wx.cp.handler;

import java.util.Map;

import com.github.binarywang.demo.wx.cp.menu.TagMenu;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.chanjar.weixin.common.api.WxConsts.MenuButtonType;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler extends AbstractHandler {
    @Autowired
    TagMenu tagMenu;

  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService,
                                  WxSessionManager sessionManager) {
      String msg = "";
      String msgType = wxMessage.getMsgType();
      String key = wxMessage.getEventKey();
      String event = wxMessage.getEvent();
      switch (key){
          case("HELP_MESSAGE"):{
              msg = "you click help";
              break;
          }
          case("TAGS"):{
              msg = "you click tags";
              break;
          }
          case("NEWEST"):{
              msg = "you click newest";
              break;
          }
          default:
              throw new IllegalStateException("Unexpected value: " + key);
      }

    if (MenuButtonType.VIEW.equals(wxMessage.getEvent())) {
      return null;
    }

    return WxCpXmlOutMessage.TEXT().content(msg)
        .fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
        .build();
  }

}
