package com.github.binarywang.demo.wx.cp.repository;

import com.github.binarywang.demo.wx.cp.model.Subscriber;
import org.springframework.data.repository.CrudRepository;

public interface SubscriberRepository extends CrudRepository<Subscriber, String> {
    Subscriber findByUsername(String username);
}
