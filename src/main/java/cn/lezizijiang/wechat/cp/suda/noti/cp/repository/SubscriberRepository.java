package cn.lezizijiang.wechat.cp.suda.noti.cp.repository;

import cn.lezizijiang.wechat.cp.suda.noti.cp.model.Subscriber;
import org.springframework.data.repository.CrudRepository;

public interface SubscriberRepository extends CrudRepository<Subscriber, String> {
    Subscriber findByUsername(String username);
}
