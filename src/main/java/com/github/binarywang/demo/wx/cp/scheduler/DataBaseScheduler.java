package com.github.binarywang.demo.wx.cp.scheduler;

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
                this.push(newArticles);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.error("failed to check db update");
        }


    }

    private void push(List<Long> newArticles) {

    }


}
