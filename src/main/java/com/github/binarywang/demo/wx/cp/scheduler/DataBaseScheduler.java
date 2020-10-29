package com.github.binarywang.demo.wx.cp.scheduler;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class DataBaseScheduler {
    Long lastArticleID;
    private static final Logger logger = LoggerFactory.getLogger(DataBaseScheduler.class);

    @Scheduled(cron = "0 0 8-18 * * *")
    public void update() {
        logger.info("check update");
        try {
            if (DbUtils.getLastArticleID() > lastArticleID) {
                List<Long> newArticles = DbUtils.getNewArticleID(lastArticleID);
                lastArticleID = DbUtils.getLastArticleID();
                logger.info("new article detected, start push");
                this.push(newArticles);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.error("failed to check db update");
        } catch (WxErrorException e) {
            e.printStackTrace();
            logger.info("failed to push message");
        }


    }

    private void push(List<Long> newArticles) throws SQLException, ClassNotFoundException, WxErrorException {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        WxCpProperties pr = new WxCpProperties();
        config.setCorpId(pr.getCorpId());      // 设置微信企业号的appid
        config.setCorpSecret(pr.getAppConfigs().get(0).getSecret());  // 设置微信企业号的app corpSecret
        config.setAgentId(pr.getAppConfigs().get(0).getAgentId());     // 设置微信企业号应用ID
        config.setToken(pr.getAppConfigs().get(0).getToken());       // 设置微信企业号应用的token
        config.setAesKey(pr.getAppConfigs().get(0).getAesKey());      // 设置微信企业号应用的EncodingAESKey

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        for (Long articleID : newArticles) {
            for (String tag : DbUtils.getTags(articleID)) {
                for (String username : DbUtils.getSubscribers(tag)) {
                    passiveSendMsg(WxCpConfiguration.getCpService(1000002), DbUtils.getContent(articleID), 1000002, username);
                }
            }
        }
    }


    public void passiveSendMsg(WxCpService wxCpService, String content, int agentID, String UserName) throws WxErrorException {
        WxCpMessage wxCpMessage =
            WxCpMessage
                .TEXT()
                .agentId(agentID)
                .toUser(UserName)
                .content(content)
                .build();
        wxCpService.messageSend(wxCpMessage);
    }
}
