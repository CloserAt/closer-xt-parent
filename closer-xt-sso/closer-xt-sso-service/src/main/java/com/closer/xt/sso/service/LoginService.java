package com.closer.xt.sso.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.sso.model.params.LoginParams;

public interface LoginService {
    CallResult getQRCodeUrl();//获取微信扫码的二维码链接

    /**
     * 当用户扫码授权之后，进行得登陆回调操作
     * @param loginParams
     * @return
     */
    CallResult wxLoginCallBack(LoginParams loginParams);


    /**
     * 公众号授权路径
     * @return
     */
    //String authorize();
}
