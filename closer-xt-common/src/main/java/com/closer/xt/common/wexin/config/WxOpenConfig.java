package com.closer.xt.common.wexin.config;

import org.springframework.beans.factory.annotation.Value;
import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class WxOpenConfig {
    //相关配置
    @Value("${wx.open.config.appid}")
    private String loginAppid;
    @Value("${wx.open.config.secret}")
    private String loginSecret;
    @Value("${wx.open.config.csrfKey}")
    public String csrfKey;
    @Value("${wx.open.config.redirectUrl}")
    public String redirectUrl;
    @Value("${wx.open.config.scope}")
    public String scope;
    @Value("{wx.open.config.mobileredirectUrl}")
    public String mobileRedirect;

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(loginAppid);
        wxMpConfigStorage.setSecret(loginSecret);
        service.setWxMpConfigStorage(wxMpConfigStorage);
        return service;
    }
}
