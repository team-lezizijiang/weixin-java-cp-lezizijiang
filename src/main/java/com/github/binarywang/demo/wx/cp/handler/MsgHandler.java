package com.github.binarywang.demo.wx.cp.handler;

import com.github.binarywang.demo.wx.cp.builder.TextBuilder;
import java.sql.*;

import com.github.binarywang.demo.wx.cp.utils.DbUtils;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService,
                                    WxSessionManager sessionManager) {
        final String msgType = wxMessage.getMsgType();
        //TODO 组装回复消息
        String contents = wxMessage.getContent();
        String FromUserName = wxMessage.getFromUserName();

        String content;
        StringBuffer stringBuffer = new StringBuffer();
        String[] lst = contents.split(" ");
        try {

            if("#查找关键词".equals(lst[0])){
                if(lst.length == 1){
                    stringBuffer.append("对不起，您输入的格式有误，请按照#查找关键词 关键词1 关键词2....的格式使用，中间需要加上空格");
                }
                List<Long> r;
                for (int i = 1; i < lst.length; i++) {
                    r = DbUtils.getArticleIDByTag(lst[i]);
                    // 5、执行数据库操作
                    stringBuffer.append("您查询的关键词"+lst[i]+"对应的标题有\n");
                    // 6、获取并操作结果集
                    for (Long articleID: r){
                        stringBuffer.append(DbUtils.getTitle(articleID));
                        stringBuffer.append("\n");
                    }
                    if(r.isEmpty()){
                        stringBuffer = new StringBuffer();
                        stringBuffer.append("您输入的这个关键词尚无记录");
                    }
                }
            }else if("#订阅".equals(lst[0])){
                if(lst.length == 1){
                    stringBuffer.append("对不起，您输入的格式有误，请按照#订阅 关键词1 关键词2....的格式使用，中间需要加上空格");
                }
                for (int i = 1; i < lst.length; i++) {
                    DbUtils.subscribe(FromUserName, lst[i]);
                }
                stringBuffer.append("订阅成功");
            }else if("#查询文章内容".equals(lst[0])){
                if(lst.length == 1){
                    stringBuffer.append("对不起，您输入的格式有误，请按照#查询文章内容 标题1的格式使用，中间需要加上空格");
                }
                Long articleID = 0L;
                for (int i = 1; i < lst.length; i++) {
                    articleID = DbUtils.getArticleIDByTitle(lst[i]).get(0);
                    stringBuffer.append(DbUtils.getContent(articleID));
                }
            }else{
                stringBuffer.append("您输入内容无效，请先输入#");
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        content = stringBuffer.toString();
        return new TextBuilder().build(content, wxMessage, cpService);

    }
}
