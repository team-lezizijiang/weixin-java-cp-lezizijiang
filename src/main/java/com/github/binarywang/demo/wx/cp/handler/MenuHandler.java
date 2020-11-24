package com.github.binarywang.demo.wx.cp.handler;

import com.github.binarywang.demo.wx.cp.Article;
import com.github.binarywang.demo.wx.cp.ArticleRepository;
import com.github.binarywang.demo.wx.cp.Author;
import com.github.binarywang.demo.wx.cp.AuthorRepository;
import com.github.binarywang.demo.wx.cp.builder.MyTextCardBuilder;
import com.github.binarywang.demo.wx.cp.menu.TagMenu;
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


/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler extends AbstractHandler {
    private static final Logger logger = LoggerFactory.getLogger(MenuHandler.class);
    @Autowired
    TagMenu tagMenu;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private AuthorRepository authorRepository;

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
                for (Author author : authorRepository.findAll()) {
                    msg.append(author.getName()).append("  ");// todo:　消息过长无法发送
                }
                break;
            }
            case ("NEWEST"): {
                try {
                    passiveSendMsg(cpService, articleRepository.getTopByOrderByArticleIDDesc(), wxMessage.getFromUserName());
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

    public void passiveSendMsg(WxCpService wxCpService, Article article, String UserName) throws WxErrorException, SQLException, ClassNotFoundException {
        WxCpMessage wxCpMessage =
            new MyTextCardBuilder().buildTestCardMsg(article.getAuthors().get(0).getName(), UserName, article.getTitle(), "https://message.lezizijiang.cn/content/?articleID=" + article.getArticleID(), "全文");
        wxCpService.messageSend(wxCpMessage);
    }

}
