package cn.lezizijiang.wechat.cp.suda.noti.cp.controller;

import cn.lezizijiang.wechat.cp.suda.noti.cp.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单使用网页展示通知全文
 */
@RestController
@Slf4j
@RequestMapping("/content")
public class ArticleContentController {
    public static final Logger logger = LoggerFactory.getLogger(ArticleContentController.class);
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleContentController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
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
