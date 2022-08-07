package com.closer.xt.sso.impl;

import com.closer.xt.common.constant.RedisKey;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.common.wexin.config.WxOpenConfig;
import com.closer.xt.sso.domain.LoginDomain;
import com.closer.xt.sso.domain.repository.LoginDomainRepository;
import com.closer.xt.sso.model.params.LoginParams;
import com.closer.xt.sso.service.LoginService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl extends AbstractService implements LoginService {

    @Autowired
    private LoginDomainRepository loginDomainRepository;

    @Override
    public CallResult getQRCodeUrl() {
        LoginDomain loginDomain = loginDomainRepository.createDomain(new LoginParams());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>(){
            @Override
            public CallResult<Object> doAction() {
                return loginDomain.buildQrConnectUrl();
            }
        });
    }

    @Override
    @Transactional
    public CallResult wxLoginCallBack(LoginParams loginParams) {
        LoginDomain loginDomain = loginDomainRepository.createDomain(loginParams);
        //带有事务得执行操作-记得加上事务注解
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkBiz() {
                //检查业务参数
                return loginDomain.checkWxLoginCallBackBiz();
            }

            @Override
            public CallResult<Object> doAction() {
                //写业务逻辑的，交给domain
                return loginDomain.wxLoginCallBack();
            }

            @Override
            public void finishUp(CallResult<Object> callResult) {
                //此处是记录日志的，且不能影响登陆操作，但是现在要是rocketmq突然挂掉，会影响到登陆，因此，需要改造这里的代码
                loginDomain.wxLoginCallBackFinishUp(callResult);
            }
        });
    }

//    @Override
//    public String authorize() {
//        LoginDomain loginDomain = loginDomainRepository.createDomain(new LoginParams());
//        return loginDomain.buildGzhUrl();
//
//    }
}
