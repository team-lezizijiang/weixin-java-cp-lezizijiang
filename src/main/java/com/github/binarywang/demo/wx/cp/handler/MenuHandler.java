package com.github.binarywang.demo.wx.cp.handler;

import com.github.binarywang.demo.wx.cp.builder.MyTextCardBuilder;
import com.github.binarywang.demo.wx.cp.menu.TagMenu;
import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import me.chanjar.weixin.common.api.WxConsts.MenuButtonType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Map;

import static com.github.binarywang.demo.wx.cp.utils.DbUtils.getAuthors;
import static com.github.binarywang.demo.wx.cp.utils.DbUtils.getLastArticleID;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler extends AbstractHandler {
    private static final Logger logger = LoggerFactory.getLogger(MenuHandler.class);
    @Autowired
    TagMenu tagMenu;

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService,
                                    WxSessionManager sessionManager) {
        StringBuilder msg = new StringBuilder();
        String msgType = wxMessage.getMsgType();
        String key = wxMessage.getEventKey();
        String event = wxMessage.getEvent();
        switch (key) {
            case ("HELP_MESSAGE"): {
                msg = new StringBuilder("输入#查找关键词 关键词来查找文章\n");
                msg.append("输入#订阅 标签 来订阅文章标签\n");
                msg.append("输入#查询文章内容 标题1 来查看文章内容");
                break;
            }
            case ("TAGS"): {
                try {
                    for (String tag : getAuthors()) {
                        msg.append(tag).append("  ");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
                break;
            }
            case ("NEWEST"): {
                try {
                    passiveSendMsg(cpService, getLastArticleID(), wxMessage.getFromUserName());
                    msg = new StringBuilder();
                } catch (ClassNotFoundException | SQLException | WxErrorException e) {
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

    public void passiveSendMsg(WxCpService wxCpService, Long articleID, String UserName) throws WxErrorException, SQLException, ClassNotFoundException {
        WxCpMessage wxCpMessage =
            new MyTextCardBuilder().buildTestCardMsg(getAuthors(articleID).get(0), UserName, DbUtils.getTitle(articleID), "https://message.lezizijiang.cn/content/?articleID=" + articleID, "全文");
        wxCpService.messageSend(wxCpMessage);
    }

}
