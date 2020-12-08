package cn.lezizijiang.wechat.cp.suda.noti.cp.repository;

import cn.lezizijiang.wechat.cp.suda.noti.cp.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, String> {
    Author findByName(String name);
}
