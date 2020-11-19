package com.github.binarywang.demo.wx.cp.controller;

import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * 简单使用网页展示通知全文
 */
@RestController
@Slf4j
public class ArticleContentController {
    public static final Logger logger = LoggerFactory.getLogger(ArticleContentController.class);


    @RequestMapping("/content")
    public String showContent(@RequestParam("articleID") String articleID) {
        Long article_id = Long.parseLong(articleID);
        String content;
        try {
            content = DbUtils.getContent(article_id);
            content = content.replace("\n", "<br>"); //格式化
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getLocalizedMessage());
            content = e.getMessage();
        }
        return content;
    }
}
