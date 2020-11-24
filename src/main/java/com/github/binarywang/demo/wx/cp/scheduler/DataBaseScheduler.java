package com.github.binarywang.demo.wx.cp.scheduler;

import com.github.binarywang.demo.wx.cp.Article;
import com.github.binarywang.demo.wx.cp.ArticleRepository;
import com.github.binarywang.demo.wx.cp.Author;
import com.github.binarywang.demo.wx.cp.Subscriber;
import com.github.binarywang.demo.wx.cp.builder.MyTextCardBuilder;
import com.github.binarywang.demo.wx.cp.config.WxCpConfiguration;
import com.github.binarywang.demo.wx.cp.config.WxCpProperties;
import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
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
    private static Long lastArticleID;
    @Autowired
    private static ArticleRepository articleRepository;

    static {
        try {
            lastArticleID = DbUtils.getLastArticleID();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    } // 初始化为最新

    @Autowired
    private WxCpProperties pr;

    @Scheduled(cron = "0 0 8-18 * * *")
    public void update() {
        logger.info("check update");
        try {
            List<Article> newArticles = articleRepository.findAllByPubdateAfter(new Date(lastArticleID));
            if (newArticles.size() > 0) {
                logger.info("new article detected, start push");
                this.push(newArticles); // 检测到最新文章， 更新并向订阅用户推送
            } else {
                logger.info("already up to date.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("failed to check db update");
        } catch (WxErrorException e) {
            e.printStackTrace();
            logger.error("failed to push message");
        }


    }

    private void push(List<Article> newArticles) throws SQLException, ClassNotFoundException, WxErrorException {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(pr.getCorpId());      // 设置微信企业号的appid
        config.setCorpSecret(pr.getAppConfigs().get(0).getSecret());  // 设置微信企业号的app corpSecret
        config.setAgentId(pr.getAppConfigs().get(0).getAgentId());     // 设置微信企业号应用ID
        config.setToken(pr.getAppConfigs().get(0).getToken());       // 设置微信企业号应用的token
        config.setAesKey(pr.getAppConfigs().get(0).getAesKey());      // 设置微信企业号应用的EncodingAESKey

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        for (Article article : newArticles) { // 新文章
            for (Author author : article.getAuthors()) { //文章标签
                for (Subscriber subscriber : author.getSubscribers()) { // 订阅用户
                    passiveSendMsg(WxCpConfiguration.getCpService(1000002), author.getName(), article, subscriber.getUsername()); //主动发送卡片消息，展示
                }
            }
        }
    }

    public void passiveSendMsg(WxCpService wxCpService, String title, Article article, String UserName) throws WxErrorException, SQLException, ClassNotFoundException {
        WxCpMessage wxCpMessage =
            new MyTextCardBuilder().buildTestCardMsg(title, UserName, article.getTitle(), "https://message.lezizijiang.cn/content/?articleID=" + article.getArticleID(), "全文");
        wxCpService.messageSend(wxCpMessage);
    }
}
