package com.github.binarywang.demo.wx.cp.controller;

import com.github.binarywang.demo.wx.cp.Author;
import com.github.binarywang.demo.wx.cp.AuthorRepository;
import com.github.binarywang.demo.wx.cp.Subscriber;
import com.github.binarywang.demo.wx.cp.SubscriberRepository;
import com.github.binarywang.demo.wx.cp.config.WxCpProperties;
import com.github.binarywang.demo.wx.cp.model.TagModel;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@Controller
@Slf4j
public class TagSubscribeController {
    public static final Logger logger = LoggerFactory.getLogger(ArticleContentController.class);
    @Autowired
    private WxCpProperties pr;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;


    @RequestMapping("/tags")
    public String showTags(@RequestParam("code") String code, @RequestParam("state") int state, Model model) throws SQLException, ClassNotFoundException {
        String username = getUsername(code);
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
        WxCpService service;
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(pr.getCorpId());      // 设置微信企业号的appid
        config.setCorpSecret(pr.getAppConfigs().get(0).getSecret());  // 设置微信企业号的app corpSecret
        config.setAgentId(pr.getAppConfigs().get(0).getAgentId());     // 设置微信企业号应用ID
        config.setToken(pr.getAppConfigs().get(0).getToken());       // 设置微信企业号应用的token
        config.setAesKey(pr.getAppConfigs().get(0).getAesKey());      // 设置微信企业号应用的EncodingAESKey
        service = new WxCpServiceImpl();
        service.setWxCpConfigStorage(config);


        WxCpOauth2UserInfo wxCpOauth2UserInfo = null;
        try {
            wxCpOauth2UserInfo = service.getOauth2Service().getUserInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        String[] res = new String[3];
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
            return "fail";
        }
        for (String tag : tagList) {
            if (tag.equals(userName)) {
                continue;
            }
            Set<Author> authors = new HashSet<>();
            authors.add(authorRepository.findByName(tag));
            subscriber.setAuthors(authors);
            subscriberRepository.save(subscriber);
        }
        return "success";
    }
}
