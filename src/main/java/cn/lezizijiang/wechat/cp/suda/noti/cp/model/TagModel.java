package cn.lezizijiang.wechat.cp.suda.noti.cp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagModel {
    public String author;
    public boolean isChecked;

    public TagModel(String author) {
        this.author = author;
        this.isChecked = false;
    }

}
