package com.github.binarywang.demo.wx.cp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagModel {
    String author;
    boolean isChecked;
    public TagModel(String author){
        this.author = author;
        this.isChecked = false;
    }

}
