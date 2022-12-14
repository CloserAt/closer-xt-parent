package com.closer.xt.sso.domain;

import com.closer.xt.common.utils.JWTUtils;
import com.closer.xt.common.constant.RedisKey;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.sso.dao.data.User;
import com.closer.xt.sso.dao.mongo.data.UserLog;
import com.closer.xt.sso.domain.repository.LoginDomainRepository;
import com.closer.xt.sso.domain.thread.InviteThread;
import com.closer.xt.sso.model.enums.LoginType;
import com.closer.xt.sso.model.params.LoginParams;
import com.closer.xt.sso.model.params.UserParams;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/*
专门处理和登陆相关得操作
 */
@Slf4j
public class LoginDomain {

    private LoginDomainRepository loginDomainRepository;

    private LoginParams loginParams;

    public static final String secretKey = "closer@#$%shuahfqu%$%";//使用JWT技术需要传入得key，也可以写在配置文件中，但是定义在类中较为安全

    public LoginDomain(LoginDomainRepository loginDomainRepository, LoginParams loginParams) {
        this.loginDomainRepository = loginDomainRepository;
        this.loginParams = loginParams;
    }

    public CallResult<Object> buildQrConnectUrl() {
        String url = this.loginDomainRepository.buildQrUrl();
        return CallResult.success(url);
    }

    public CallResult<Object> checkWxLoginCallBackBiz() {
        //主要检查state是否合法，即csrfkey得检测==如何检查？答：在创建传入csrfkey之前先存入redis中并设置有效期
        String state = loginParams.getState();
        //去redis检查是否存在key=state得值
        boolean isVerify = loginDomainRepository.checkState(state);
        if (!isVerify) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getMsg());
        }
        return CallResult.success();
    }
    private User userSave;
    private boolean isNewer;
    public CallResult<Object> wxLoginCallBack() {
        String code = loginParams.getCode();
        try {
            //0.redistemplate检查refreshToken是否存在，存在就直接获取accessToken，无需重新授权
            //refreshToken只有在登录状态下才能有效
            //refreshToken无法应用于用户登陆，刷新token来加快访问，只能用于拿到登陆用户信息的场景
            //String refreshToken = loginDomainRepository.stringRedisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN);
            String refreshToken = null;
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
            if (refreshToken == null) {
                //1.不能存在，通过code获取到accessToken，accessToken获取refreshToken
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpService.oauth2getAccessToken(code);
                refreshToken = wxMpOAuth2AccessToken.getRefreshToken();
                String unionId = wxMpOAuth2AccessToken.getUnionId();//bug修复：给refreshToken标识上唯一的id,防止所有用户公用一个refreshToken
                //refreshToken,需要保存到redis当中，过期时间设置为28天
                loginDomainRepository.stringRedisTemplate.opsForValue().set(RedisKey.REFRESH_TOKEN + unionId, refreshToken, 28, TimeUnit.DAYS);
            } else {
                //2.下次登陆得refreshToken存在，则直接获取accessToken，不需要用户重新授权
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpService.oauth2refreshAccessToken(refreshToken);
            }
            //3.通过accessToken获取微信用户信息（openid, unionId）unionId在web端，公众号端，手机是唯一得
            WxMpUser wxMpUser = loginDomainRepository.wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
            String unionId = wxMpUser.getUnionId();
            //4.判断unionId在数据库得user表中 是否存在 存在就更新最后登陆时间 不存在就注册
            User user = this.loginDomainRepository.createUserDomain(new UserParams()).findUserByUnionId(unionId);
            boolean isNew = false;
            if (user == null) {
                //不存在就注册
                Long currentTimeMillis = System.currentTimeMillis();
                user = new User();
                user.setNickname(wxMpUser.getNickname());
                user.setHeadImageUrl(wxMpUser.getHeadImgUrl());
                user.setSex(wxMpUser.getSex());
                user.setOpenId(wxMpUser.getOpenId());
                user.setLoginType(LoginType.WX.getCode());
                user.setCountry(wxMpUser.getCountry());
                user.setCity(wxMpUser.getCity());
                user.setProvince(wxMpUser.getProvince());
                user.setRegisterTime(currentTimeMillis);
                user.setLastLoginTime(currentTimeMillis);
                user.setUnionId(wxMpUser.getUnionId());
                user.setArea("");
                user.setMobile("");
                user.setGrade("");
                user.setName(wxMpUser.getNickname());
                user.setSchool("");
                this.loginDomainRepository.createUserDomain(new UserParams()).saveUser(user);
                isNew = true;

                //新用户
                //查看是否有邀请信息
                fillInvite(user);
            }

            //5.使用jwt技术生成token,需要把token存储起来
            String token = JWTUtils.createJWT(7 * 24 * 60 * 60 * 1000, user.getId(), secretKey);
            log.info("生成的token是："+token);
            log.info("userId是："+user.getId());
            loginDomainRepository.stringRedisTemplate.opsForValue().set(RedisKey.TOKEN + token,String.valueOf(user.getId()),7,TimeUnit.DAYS);

            //6.假设用户只能在一个端进行登陆，其他端再登陆直接踢下线
            String oldToken = loginDomainRepository.stringRedisTemplate.opsForValue().get(RedisKey.LOGIN_USER_TOKEN + user.getId());
            if (oldToken != null) {
                //说明登陆过了
                //在用户进行登陆验证时，需要先验证token是否合法，然后去redis查询是否存在token 不存在就代表不合法
                loginDomainRepository.stringRedisTemplate.delete(RedisKey.TOKEN + oldToken);
            }
            loginDomainRepository.stringRedisTemplate.opsForValue().set(RedisKey.LOGIN_USER_TOKEN + user.getId(), token);
            //7.返回给前端token,存在cookie当中，下次请求时从cookie中获取token
            HttpServletResponse response = loginParams.getResponse();
            Cookie cookie = new Cookie("t_token",token);
            cookie.setMaxAge(8*24*3600);
            cookie.setPath("/");
            response.addCookie(cookie);
            log.info("cookie是："+String.valueOf(cookie));
            //8.拓展功能（用户积分，成就，任务）
            //9.记录日志，记录当前用户得登陆行为:MQ+MongoDB
            //10.更新用户得最后登陆时间
            if (!isNew) {
                user.setLastLoginTime(System.currentTimeMillis());//可以放入日志队列中，以免影响主进程得操作
                this.loginDomainRepository.createUserDomain(new UserParams()).updateUser(user);
            }
            this.isNewer = isNew;
            this.userSave = user;
            return CallResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return CallResult.fail(BusinessCodeEnum.LOGIN_WX_NOT_USER_INFO.getCode(), BusinessCodeEnum.LOGIN_WX_NOT_USER_INFO.getMsg());
        }
    }

    //检查是否有邀请信息
    private void fillInvite(User user) {
        //个人链接可能有多个，此处用list
        HttpServletRequest request = this.loginParams.getRequest();
        Cookie[] cookies = request.getCookies();
        List<Map<String,String>> inviteMapList = new ArrayList<>();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String[] str = name.split("_i_ga_b_");
            if (2 == str.length) {
                String billType = str[0];
                Map<String,String> map = new HashMap<>();
                map.put("billType",billType);
                map.put("userId",cookie.getValue());
                inviteMapList.add(map);
            }
        }

        //由于邀请是非必要的，不能影响登陆逻辑，所以此处是可以拆分的
        //拆分业务逻辑的方法：1.mq,把它当成消息发送出去  2.线程池
        this.loginDomainRepository.inviteThread.fillInvite(inviteMapList,user);
    }


    //记录日志
    public void wxLoginCallBackFinishUp(CallResult<Object> callResult) {
        UserLog userLog = new UserLog();
        userLog.setUserId(userSave.getId());
        userLog.setNewer(isNewer);
        userLog.setSex(userSave.getSex());
        userLog.setRegisterTime(userSave.getRegisterTime());
        userLog.setLastLoginTime(userSave.getLastLoginTime());

        //此处是一个同步操作，如果这里代码出现异常（例如rocketmq挂掉），会影响登陆
        //解决方案：方法一：使用异步  方法二：使用线程池
        this.loginDomainRepository.recordLoginUserLog(userLog);


    }

//    public String buildGzhUrl() {
//        String url = this.loginDomainRepository.buildGzhUrl();
//        return  url;
//    }
}
