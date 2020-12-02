package com.github.binarywang.demo.wx.cp.controller;

import com.github.binarywang.demo.wx.cp.config.WxCpConfiguration;
import com.github.binarywang.demo.wx.cp.config.WxCpProperties;
import com.github.binarywang.demo.wx.cp.model.Author;
import com.github.binarywang.demo.wx.cp.model.Subscriber;
import com.github.binarywang.demo.wx.cp.model.TagModel;
import com.github.binarywang.demo.wx.cp.repository.AuthorRepository;
import com.github.binarywang.demo.wx.cp.repository.SubscriberRepository;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Slf4j
public class TagSubscribeController {
    public static final Logger logger = LoggerFactory.getLogger(ArticleContentController.class);
    private final WxCpService service;
    private final AuthorRepository authorRepository;
    private final SubscriberRepository subscriberRepository;


    @Autowired
    public TagSubscribeController(WxCpProperties pr, AuthorRepository authorRepository, SubscriberRepository subscriberRepository) {
        super();
        this.authorRepository = authorRepository;
        this.subscriberRepository = subscriberRepository;
        this.service = WxCpConfiguration.getCpService(pr.getAppConfigs().get(0).getAgentId());
    }


    @RequestMapping("/tags")
    public String showTags(@RequestParam(value = "code", defaultValue = "0") String code, @RequestParam("state") int state, Model model) {
        String username;
        if (code.equals("0")) {
            username = "debug";
        } else {
            username = getUsername(code);
        }
        model.addAttribute("username", username);
        List<TagModel> modelList = new ArrayList<>();
        Set<Author> subscribed;
        try {
            subscribed = subscriberRepository.findByUsername(username).getAuthors();
        } catch (NullPointerException e) {
            Subscriber subscriber = new Subscriber();
            subscriber.setUser_name(username);
            subscriber.setId((int) (subscriberRepository.count() + 1));
            subscribed = new HashSet<>();
            subscriberRepository.save(subscriber);
        }
        logger.info(username + "logged in");
        for (Author author : authorRepository.findAll()) {
            TagModel tag = new TagModel(author.getName());
            if (subscribed.contains(author))
                tag.setChecked(true);
            modelList.add(tag);
        }
        model.addAttribute("AllTags", modelList);
        return "tags";
    } // todo: 使用checkbox的tag订阅器

    private String getUsername(String code) {
        WxCpOauth2UserInfo wxCpOauth2UserInfo = null;
        try {
            wxCpOauth2UserInfo = service.getOauth2Service().getUserInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
            return "登陆错误";
        }
        String[] res = new String[3];
        assert wxCpOauth2UserInfo != null;
        res[0] = wxCpOauth2UserInfo.getUserId();
        res[1] = wxCpOauth2UserInfo.getDeviceId();
        res[2] = wxCpOauth2UserInfo.getOpenId();
        return res[0];
    }

    @ResponseBody
    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public String dealWithSubscribe(@RequestBody() String[] tagList) {
        logger.info(Arrays.toString(tagList));
        String userName = tagList[0];
        Subscriber subscriber = subscriberRepository.findByUsername(userName);
        if (tagList.length <= 1) {
            return "{\"fail\"}";
        }
        Set<Author> authors = new HashSet<>();
        for (String tag : tagList) {
            if (tag.equals(userName)) {
                continue;
            }
            authors.add(authorRepository.findByName(tag));
        }
        subscriber.setAuthors(authors);
        subscriberRepository.save(subscriber);
        return "{\"success\"}";
    }
}
