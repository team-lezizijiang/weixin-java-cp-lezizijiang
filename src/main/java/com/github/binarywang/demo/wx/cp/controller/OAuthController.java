package com.github.binarywang.demo.wx.cp.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
@Slf4j
public class OAuthController{
    WxCpService wxCpService;
    private final Logger logger = LoggerFactory.getLogger(WxPortalController.class);

    /**
     * 第三方登录跳转
     *
     * */
    @RequestMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException{
        wxCpService.getOauth2Service().buildAuthorizationUrl("www.lezizijiang.com", "200");
    }
    @RequestMapping("/callback/{source}")
    public String[] getAuthInfo(@PathVariable("source") String source, @RequestParam("code") String code) throws WxErrorException {
        WxCpOauth2UserInfo wxCpOauth2UserInfo= wxCpService.getOauth2Service().getUserInfo(code);
        String[] res = new String[3];
        res[0] = wxCpOauth2UserInfo.getUserId();
        res[1] = wxCpOauth2UserInfo.getDeviceId();
        res[2] = wxCpOauth2UserInfo.getUserTicket();
    }
    @RequestMapping("/avatar")
    public String avatar(){

    }

}
