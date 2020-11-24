package com.github.binarywang.demo.wx.cp;

import org.springframework.data.repository.CrudRepository;

public interface SubscriberRepository extends CrudRepository<Subscriber, String> {
    Subscriber findByUsername(String username);
}
