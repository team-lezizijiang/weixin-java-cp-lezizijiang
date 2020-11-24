package com.github.binarywang.demo.wx.cp.handler;

import com.github.binarywang.demo.wx.cp.Article;
import com.github.binarywang.demo.wx.cp.ArticleRepository;
import com.github.binarywang.demo.wx.cp.AuthorRepository;
import com.github.binarywang.demo.wx.cp.SubscriberRepository;
import com.github.binarywang.demo.wx.cp.builder.MyTextCardBuilder;
import com.github.binarywang.demo.wx.cp.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService,
                                    WxSessionManager sessionManager) {
        final String msgType = wxMessage.getMsgType();
        String contents = wxMessage.getContent();
        String FromUserName = wxMessage.getFromUserName();

        String content;
        StringBuffer stringBuffer = new StringBuffer();
        String[] lst = contents.split(" ");
        try {

            if ("#查找关键词".equals(lst[0])) {
                if (lst.length == 1) {
                    stringBuffer.append("对不起，您输入的格式有误，请按照#查找关键词 关键词1 关键词2....的格式使用，中间需要加上空格");
                }
                List<Article> r;
                for (int i = 1; i < lst.length; i++) {
                    r = articleRepository.findAllByAuthorsContains(authorRepository.findByName(lst[i]));
                    // 5、执行数据库操作
                    stringBuffer.append("您查询的关键词" + lst[i] + "对应的标题有\n");
                    // 6、获取并操作结果集
                    for (Article article : r) {
                        stringBuffer.append(article.getTitle());
                        stringBuffer.append("\n");
                    }
                    if (r.isEmpty()) {
                        stringBuffer = new StringBuffer();
                        stringBuffer.append("您输入的这个关键词尚无记录");
                    }
                }
            } else if ("#订阅".equals(lst[0])) {
                if (lst.length == 1) {
                    stringBuffer.append("对不起，您输入的格式有误，请按照#订阅 关键词1 关键词2....的格式使用，中间需要加上空格");
                }
                for (int i = 1; i < lst.length; i++) {
                    subscriberRepository.findByUsername(FromUserName).getAuthors().add(authorRepository.findByName(lst[i]));
                }
                stringBuffer.append("订阅成功");
            } else if ("#查询文章内容".equals(lst[0])) {
                if (lst.length == 1) {
                    stringBuffer.append("对不起，您输入的格式有误，请按照#查询文章内容 标题1的格式使用，中间需要加上空格");
                }
                Article article = null;
                for (int i = 1; i < lst.length; i++) {
                    article = articleRepository.findByTitle(lst[i]);
                    try {
                        passiveSendMsg(cpService, "正文内容", article, wxMessage.getFromUserName());
                    } catch (WxErrorException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
            } else {
                stringBuffer.append("您输入内容无效，请先输入#");
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        content = stringBuffer.toString();
        return new TextBuilder().build(content, wxMessage, cpService);
    }

    public void passiveSendMsg(WxCpService wxCpService, String title, Article article, String UserName) throws WxErrorException {
        WxCpMessage wxCpMessage =
            new MyTextCardBuilder().buildTestCardMsg(title, UserName, article.getTitle(), "https://message.lezizijiang.cn/content/?articleID=" + article.getArticleID(), "全文");
        wxCpService.messageSend(wxCpMessage);

    }
}
