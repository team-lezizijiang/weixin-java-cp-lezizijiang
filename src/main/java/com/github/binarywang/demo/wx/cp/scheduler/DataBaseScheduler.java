package com.github.binarywang.demo.wx.cp.scheduler;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;

public class DataBaseScheduler {
    public void passiveSendMsg(WxCpService wxCpService, String content, int agentID, String UserName) throws WxErrorException {
        WxCpMessage wxCpMessage =
            WxCpMessage
                .TEXT()
                .agentId(agentID)
                .toUser(UserName)
                .content(content)
                .build();
        wxCpService.messageSend(wxCpMessage);
    }
}
