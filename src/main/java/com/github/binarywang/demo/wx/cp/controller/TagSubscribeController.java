package com.github.binarywang.demo.wx.cp.controller;

import com.github.binarywang.demo.wx.cp.config.WxCpProperties;
import com.github.binarywang.demo.wx.cp.model.TagModel;
import com.github.binarywang.demo.wx.cp.utils.DbUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.HTML;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class TagSubscribeController {
    public static final Logger logger = LoggerFactory.getLogger(ArticleContentController.class);
    @Autowired
    private WxCpProperties pr;


    @RequestMapping("/tags")
    public String showTags(@RequestParam("code") String code, @RequestParam("state") int state, Model model) throws SQLException, ClassNotFoundException {
        String username = getUsername(code);
        model.addAttribute("username", username);
        List<TagModel> modelList = new ArrayList<>();
        List<String> tags = DbUtils.getAuthors();
        List<String> subscribed = DbUtils.getTagBySubscriber(username);
        for(int i = 0; i < tags.size(); i++){
            TagModel tag = new TagModel(tags.get(i));
            if(subscribed.contains(tag.getAuthor()))
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

}
