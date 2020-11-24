package com.github.binarywang.demo.wx.cp.menu;

import com.github.binarywang.demo.wx.cp.config.WxCpProperties;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpMenuService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMenuServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagMenu {
    String json; // 设置自定义菜单
    WxMenu menu;

    @Autowired
    public TagMenu(WxCpProperties pr) throws WxErrorException {

        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(pr.getCorpId());      // 设置微信企业号的appid
        config.setCorpSecret(pr.getAppConfigs().get(0).getSecret());  // 设置微信企业号的app corpSecret
        config.setAgentId(pr.getAppConfigs().get(0).getAgentId());     // 设置微信企业号应用ID
        config.setToken(pr.getAppConfigs().get(0).getToken());       // 设置微信企业号应用的token
        config.setAesKey(pr.getAppConfigs().get(0).getAesKey());      // 设置微信企业号应用的EncodingAESKey
        WxCpService service = new WxCpServiceImpl();
        service.setWxCpConfigStorage(config);

        json = "{\"menu\": {\n" +
            " 	\"button\":[\n" +
            " 	{	\n" +
            "    	\"type\":\"click\",\n" +
            "    	\"name\":\" 使用说明 \",\n" +
            "     	\"key\":\"HELP_MESSAGE\" \n" +
            "	},\n" +
            "	{ \n" +
            "		\"name\":\" 菜单 \",\n" +
            "		\"sub_button\":[\n" +
            "		{	\n" +
            "			\"type\":\"view\",\n" +
            "			\"name\":\" 标签群 \",\n" +
            "			\"url\":\"" + service.getOauth2Service().buildAuthorizationUrl("https://message.lezizijiang.cn/tags", "200") + "\"\n" +
            "		},\n" +
            "		{\n" +
            "			\"type\":\"click\",\n" +
            "			\"name\":\" 最新新闻 \",\n" +
            "			\"key\":\"NEWEST\"\n" +
            "		}]\n" +
            "   }]\n" +
            "}}";
        menu = WxMenu.fromJson(json);
        WxCpMenuService menuService = new WxCpMenuServiceImpl(service);
        menuService.create(menu);
    }
}
