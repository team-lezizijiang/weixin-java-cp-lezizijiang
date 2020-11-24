package com.github.binarywang.demo.wx.cp;

import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findAllByPubdateAfter(Date pubdate);

    Article getTopByOrderByArticleIDDesc();

    List<Article> findAllByAuthorsContains(Author author);

    Article findByTitle(String title);
}
