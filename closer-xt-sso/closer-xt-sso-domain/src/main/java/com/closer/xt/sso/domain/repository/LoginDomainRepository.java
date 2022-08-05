package com.closer.xt.sso.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.closer.xt.common.constant.RedisKey;
import com.closer.xt.common.wexin.config.WxOpenConfig;
import com.closer.xt.sso.dao.UserMapper;
import com.closer.xt.sso.dao.data.User;
import com.closer.xt.sso.domain.LoginDomain;
import com.closer.xt.sso.domain.UserDomain;
import com.closer.xt.sso.model.params.LoginParams;
import com.closer.xt.sso.model.params.UserParams;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginDomainRepository {
    @Autowired
    public WxMpService wxMpService;

    @Autowired
    private WxOpenConfig wxOpenConfig;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserDomainRepository userDomainRepository;

    public LoginDomain createDomain(LoginParams loginParams) {
        return new LoginDomain(this,loginParams);
    }

    public boolean checkState(String state) {
        Boolean hasKey = stringRedisTemplate.hasKey(RedisKey.WX_STATE_KEY + state);
        return hasKey != null && hasKey;
    }

    public String buildQrUrl() {
        //生成state参数方式1，用于防范csrf
//        String csrfKey = wxOpenConfig.getCsrfKey();
//        String time = new DateTime().toString("yyyyMMddHHmmss");
//        csrfKey = csrfKey+ "_" + time;//防止被猜到

        //生成state参数方式2
        String csrfKey = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(RedisKey.WX_STATE_KEY + csrfKey, "1", 60, TimeUnit.SECONDS);
        /*
        Cross-site request forgery ：csrf 跨站伪造攻击
        http://xxxx/sso/login/wxLoginCallBack？state=csrfKey
        在url里验证csrf是不是我们存储的，是则证明连接安全
        典型场景：在访问银行网站时又访问了其他网站并点击，此时银行账户资金被操作
         */
        //String state = DigestUtils.md5Hex(wxOpenConfig.csrfKey+time);
        String url = wxMpService.buildQrConnectUrl(wxOpenConfig.getRedirectUrl(), wxOpenConfig.getScope(), csrfKey);

        return url;
    }


    public UserDomain createUserDomain(UserParams userParams) {
        return userDomainRepository.createDomain(userParams);
    }

//    public String buildGzhUrl() {
//        String csrfKey = UUID.randomUUID().toString();
//        stringRedisTemplate.opsForValue().set(RedisKey.WX_STATE_KEY + csrfKey, "1", 60, TimeUnit.SECONDS);
//        String url = wxMpService.buildQrConnectUrl(wxOpenConfig.getRedirectUrl(), WxConsts.OAuth2Scope.SNSAPI_USERINFO, csrfKey);
//        return url;
//    }
}
