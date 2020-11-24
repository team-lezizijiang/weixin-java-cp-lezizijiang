package com.github.binarywang.demo.wx.cp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "author")
public class Author {
    @GeneratedValue(strategy = GenerationType.AUTO)
    String name;
    @Id
    long authorID;
    @ManyToMany(targetEntity = Article.class, mappedBy = "authors")
    Set<Article> articles = new HashSet<Article>();
    @ManyToMany
    @JoinTable(name = "s_a",// 使用 @JoinTable 标签的 name 属性注解第三方表名称
        joinColumns = {@JoinColumn(name = "authorID")},// 使用 joinColumns 属性来注解当前实体类在第三方表中的字段名称并指向该对象\
        inverseJoinColumns = {@JoinColumn(name = "subscriberID")}// 使用 inverseJoinColumns 属性来注解当前实体类持有引用对象在第三方表中的字段名称并指向被引用对象表
    )
    Set<Subscriber> subscribers = new HashSet<>();

    public Author(String s) {
        name = s;
    }

    public Author() {

    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(long authorID) {
        authorID = authorID;
    }


    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Author) obj).name == this.name;
    }
}
