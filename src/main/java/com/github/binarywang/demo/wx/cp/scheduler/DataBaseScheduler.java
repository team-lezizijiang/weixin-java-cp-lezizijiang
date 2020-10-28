package com.github.binarywang.demo.wx.cp.scheduler;

import me.chanjar.weixin.common.error.WxErrorException;
import com.github.binarywang.demo.wx.cp.config.WxCpConfiguration;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.List;

@Component
public class DataBaseScheduler {
    private Logger logger = LoggerFactory.getLogger(DataBaseScheduler.class);
    Long lastArticleID;
    @Scheduled(cron = "0 0 8-18 * * *")
    public void update(){
        try {
            if (DbUtils.getLastArticleID() > lastArticleID){
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
        for (Long articleID: newArticles){
            for (String tag: DbUtils.getTags(articleID)){
                for (String username: DbUtils.getSubscribers(tag)){
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
