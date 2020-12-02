package com.github.binarywang.demo.wx.cp.controller;

import com.github.binarywang.demo.wx.cp.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单使用网页展示通知全文
 */
@RestController
@Slf4j
public class ArticleContentController {
    public static final Logger logger = LoggerFactory.getLogger(ArticleContentController.class);
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleContentController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @RequestMapping("/content")
    public String showContent(@RequestParam("articleID") String articleID) {
        Long article_id = Long.parseLong(articleID);
        String content;
        if (articleRepository.findById(article_id).isPresent()) {
            content = articleRepository.findById(article_id).get().getContent();
            content = content.replace("\n", "<br>"); //格式化
        } else {
            content = "错误，数据库中不存在该文章";
            logger.warn("错误，数据库中不存在该文章" + articleID);
        }

        return content;
    }
}
