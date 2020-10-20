package com.github.binarywang.demo.wx.cp.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.binarywang.demo.wx.cp.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "wechat.cp")
public class WxCpProperties {
  /**
   * 设置企业微信的corpId
   */
  private String corpId = "ww0db1bc3ecc363cfc";

  private List<AppConfig> appConfigs;

  @Getter
  @Setter
  public static class AppConfig {
    /**
     * 设置企业微信应用的AgentId
     */
    private Integer agentId = 1000002;

    /**
     * 设置企业微信应用的Secret
     */
    private String secret = "oCVhYswuy8D9dcsKsWFswA7M913oVezqp72y4uJ9R-I";

    /**
     * 设置企业微信应用的token
     */
    private String token = "3hVOIUc7TAlPZ6pk46pblwbj3hl5Uz";

    /**
     * 设置企业微信应用的EncodingAESKey
     */
    private String aesKey = "MsqxNquQnFpaJ3bLoB43hRYhgYE6PkWnMfeUIjIcLjg";

  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
