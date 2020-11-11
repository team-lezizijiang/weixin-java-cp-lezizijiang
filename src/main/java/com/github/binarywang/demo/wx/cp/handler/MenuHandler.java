package com.github.binarywang.demo.wx.cp.handler;

import java.sql.SQLException;
import java.util.Map;

import com.github.binarywang.demo.wx.cp.menu.TagMenu;
import com.github.binarywang.demo.wx.cp.scheduler.DataBaseScheduler;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.chanjar.weixin.common.api.WxConsts.MenuButtonType;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;

import static com.github.binarywang.demo.wx.cp.utils.DbUtils.*;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler extends AbstractHandler {
    @Autowired
    TagMenu tagMenu;
    private static final Logger logger = LoggerFactory.getLogger(MenuHandler.class);

  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService,
                                  WxSessionManager sessionManager) {
      StringBuilder msg = new StringBuilder();
      String msgType = wxMessage.getMsgType();
      String key = wxMessage.getEventKey();
      String event = wxMessage.getEvent();
      switch (key){
          case("HELP_MESSAGE"):{
              msg = new StringBuilder("输入#查找关键词 关键词来查找文章\n");
              msg.append("输入#订阅 标签 来订阅文章标签\n");
              msg.append("输入#查询文章内容 标题1 来查看文章内容");
              break;
          }
          case("TAGS"):{
              try{
                  for (String tag: getAuthors()){
                      msg.append(tag).append("  ");// todo:　消息过长无法发送
                  }
              } catch (SQLException | ClassNotFoundException e) {
                  e.printStackTrace();
                  logger.error(e.getMessage());
              }
              break;
          }
          case("NEWEST"):{
              try {
                  msg = new StringBuilder(getTitle(getLastArticleID()));
              } catch (ClassNotFoundException | SQLException e) {
                  e.printStackTrace();
                  logger.error(e.getMessage());
              }
              break;
          }
          default:
              throw new IllegalStateException("Unexpected value: " + key);
      }

    if (MenuButtonType.VIEW.equals(wxMessage.getEvent())) {
        return null;
    }

    return WxCpXmlOutMessage.TEXT().content(msg.toString())
        .fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
        .build();
  }

}
