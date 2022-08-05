package com.closer.xt.web.api;

import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.CallResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course")
public class CourseAPI {

    @PostMapping("login")
    public CallResult courseList() {
        return CallResult.success(UserThreadLocal.get());
    }
}
