package com.github.binarywang.demo.wx.cp.scheduler;

import com.github.binarywang.demo.wx.cp.builder.MyTextCardBuilder;
import com.github.binarywang.demo.wx.cp.config.WxCpConfiguration;
import com.github.binarywang.demo.wx.cp.config.WxCpProperties;
import com.github.binarywang.demo.wx.cp.model.Article;
import com.github.binarywang.demo.wx.cp.model.Author;
import com.github.binarywang.demo.wx.cp.model.Subscriber;
import com.github.binarywang.demo.wx.cp.repository.ArticleRepository;
import com.github.binarywang.demo.wx.cp.repository.AuthorRepository;
import com.github.binarywang.demo.wx.cp.repository.SubscriberRepository;
import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Component
public class DataBaseScheduler {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseScheduler.class);
    private static Article lastArticle;

    private final WxCpService service;
    private final AuthorRepository authorRepository;
    private final SubscriberRepository subscriberRepository;
    private final ArticleRepository articleRepository;


    @Autowired
    public DataBaseScheduler(WxCpProperties pr, AuthorRepository authorRepository, SubscriberRepository subscriberRepository, ArticleRepository articleRepository) {
        super();
        this.authorRepository = authorRepository;
        this.subscriberRepository = subscriberRepository;
        this.service = WxCpConfiguration.getCpService(pr.getAppConfigs().get(0).getAgentId());
        this.articleRepository = articleRepository;
    }

    @Scheduled(cron = "0 * 8-18 * * *")
    public void update() {
        logger.info("check update");
        try {
            if (lastArticle == null){
                lastArticle = articleRepository.getTopByOrderByArticleIDDesc();
            }
            List<Article> newArticles = articleRepository.findAllWithPubdateAfter(lastArticle.getPubdate());
            lastArticle = articleRepository.getTopByOrderByArticleIDDesc();
            if (newArticles.size() > 0) {
                logger.info("new article detected, start push");
                this.push(newArticles); // 检测到最新文章， 更新并向订阅用户推送
            } else {
                logger.info("already up to date.");
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private void push(List<Article> newArticles) throws WxErrorException {
        for (Article article : newArticles) { // 新文章
            for (Author author : article.getAuthors()) { //文章标签
                for (Subscriber subscriber : author.getSubscribers()) { // 订阅用户
                    passiveSendMsg(service, author.getName(), article, subscriber.getUsername()); //主动发送卡片消息，展示
                }
            }
        }
    }

    public void passiveSendMsg(WxCpService wxCpService, String title, Article article, String UserName) throws WxErrorException {
        WxCpMessage wxCpMessage =
            new MyTextCardBuilder().buildTestCardMsg(title, UserName, article.getTitle(), "https://message.lezizijiang.cn/content/?articleID=" + article.getArticleID(), "全文");
        wxCpService.messageSend(wxCpMessage);
    }
}
