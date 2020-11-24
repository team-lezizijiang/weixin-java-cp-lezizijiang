package com.github.binarywang.demo.wx.cp;

import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, String> {
    Author findByName(String name);
}
