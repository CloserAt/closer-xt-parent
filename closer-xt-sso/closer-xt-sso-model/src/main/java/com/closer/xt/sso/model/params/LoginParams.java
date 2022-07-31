package com.closer.xt.sso.model.params;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public class LoginParams {
    private String username;
    private String password;

    //回调
    private String code;
    private String state;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private String cookieUserId;
}
