package com.closer.xt.sso.api;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserApi {

    @Autowired
    private UserService userService;

    @PostMapping("userInfo")
    public CallResult userInfo() {
        return userService.userInfo();
    }

}
