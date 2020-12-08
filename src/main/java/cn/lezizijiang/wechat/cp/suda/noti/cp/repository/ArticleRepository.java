package cn.lezizijiang.wechat.cp.suda.noti.cp.repository;

import cn.lezizijiang.wechat.cp.suda.noti.cp.model.Article;
import cn.lezizijiang.wechat.cp.suda.noti.cp.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findAllByArticleIDGreaterThan(Long ArticleId);

    Article getTopByOrderByArticleIDDesc();

    List<Article> findAllByAuthorsContains(Author author);

    Article findByTitle(String title);
}
