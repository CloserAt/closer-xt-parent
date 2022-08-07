package com.closer.xt.sso.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.closer.xt.common.constant.RedisKey;
import com.closer.xt.common.wexin.config.WxOpenConfig;
import com.closer.xt.sso.dao.UserMapper;
import com.closer.xt.sso.dao.data.User;
import com.closer.xt.sso.dao.mongo.data.UserLog;
import com.closer.xt.sso.domain.LoginDomain;
import com.closer.xt.sso.domain.UserDomain;
import com.closer.xt.sso.domain.thread.LogThread;
import com.closer.xt.sso.model.params.LoginParams;
import com.closer.xt.sso.model.params.UserParams;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
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

    //注入线程池
    @Autowired
    private LogThread logThread;

    public void recordLoginUserLog(UserLog userLog) {
        //同步发送，同时发送的消息，会自动转为json字符串

        //异步发送：asyncSend() ;此处不采用异步方式，因为虽然其性能高，但是存在丢数据的问题，但是我们不希望丢数据
        //采用同步，利用线程池
        log.info("记录日志开始时间{}",new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        logThread.recordLog(userLog);
    }

//    public String buildGzhUrl() {
//        String csrfKey = UUID.randomUUID().toString();
//        stringRedisTemplate.opsForValue().set(RedisKey.WX_STATE_KEY + csrfKey, "1", 60, TimeUnit.SECONDS);
//        String url = wxMpService.buildQrConnectUrl(wxOpenConfig.getRedirectUrl(), WxConsts.OAuth2Scope.SNSAPI_USERINFO, csrfKey);
//        return url;
//    }
}
