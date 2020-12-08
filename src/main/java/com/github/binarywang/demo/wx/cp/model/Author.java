package com.github.binarywang.demo.wx.cp.model;

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
    Set<Article> articles = new HashSet<>();
    @ManyToMany(mappedBy = "authors", fetch=FetchType.EAGER)
    Set<Subscriber> subscribers = new HashSet<>();

    public Author(String s) {
        name = s;
    }

    public Author() {

    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
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
        this.authorID = authorID;
    }


    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

}
