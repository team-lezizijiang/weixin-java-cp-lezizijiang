package com.github.binarywang.demo.wx.cp.repository;

import com.github.binarywang.demo.wx.cp.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, String> {
    Author findByName(String name);
}
