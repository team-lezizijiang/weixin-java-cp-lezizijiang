package com.github.binarywang.demo.wx.cp.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long articleID;
    @Column(name = "pub_date")
    private Date pubdate;
    private String title;
    private String content;
    private List<Author> authors = new LinkedList<>();

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToMany
    @JoinTable(name = "article_author",// 使用 @JoinTable 标签的 name 属性注解第三方表名称
        joinColumns = {@JoinColumn(name = "ArticleID")},// 使用 joinColumns 属性来注解当前实体类在第三方表中的字段名称并指向该对象
        inverseJoinColumns = {@JoinColumn(name = "authorID")}// 使用 inverseJoinColumns 属性来注解当前实体类持有引用对象在第三方表中的字段名称并指向被引用对象表
    )
    public List<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Id
    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }
}
