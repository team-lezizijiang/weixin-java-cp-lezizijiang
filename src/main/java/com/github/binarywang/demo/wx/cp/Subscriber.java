package com.github.binarywang.demo.wx.cp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subscriber")
public class Subscriber {
    @Id
    private Integer id;
    @Column(name = "user_name")
    private String username;
    @ManyToMany
    @JoinTable(name = "s_a",// 使用 @JoinTable 标签的 name 属性注解第三方表名称
        joinColumns = {@JoinColumn(name = "subscriberID")},// 使用 joinColumns 属性来注解当前实体类在第三方表中的字段名称并指向该对象\
        inverseJoinColumns = {@JoinColumn(name = "authorID")}// 使用 inverseJoinColumns 属性来注解当前实体类持有引用对象在第三方表中的字段名称并指向被引用对象表
    )
    private Set<Author> authors = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public String getUsername() {
        return username;
    }

    public void setUser_name(String user_name) {
        this.username = user_name;
    }
}
