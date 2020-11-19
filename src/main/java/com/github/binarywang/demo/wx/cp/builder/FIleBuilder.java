package com.github.binarywang.demo.wx.cp.builder;

import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMediaServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;

import java.io.File;

public class FIleBuilder {
    public WxCpMessage build(String username, WxCpService service, String mediaType, File file) throws WxErrorException {
        WxMediaUploadResult mediaUploadResult = new WxCpMediaServiceImpl(service).upload(mediaType, file);
        return WxCpMessage.FILE()
            .mediaId(mediaUploadResult.getMediaId())
            .toUser(username)
            .build();
    }
}
