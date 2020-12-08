package com.github.binarywang.demo.wx.cp.repository;

import com.github.binarywang.demo.wx.cp.model.Article;
import com.github.binarywang.demo.wx.cp.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findAllByArticleIDGreaterThan(Long ArticleId);

    Article getTopByOrderByArticleIDDesc();

    List<Article> findAllByAuthorsContains(Author author);

    Article findByTitle(String title);
}
