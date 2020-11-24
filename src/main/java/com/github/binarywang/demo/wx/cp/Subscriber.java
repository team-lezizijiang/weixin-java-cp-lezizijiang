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
    @ManyToMany(targetEntity = Author.class, mappedBy = "subscribers")
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
