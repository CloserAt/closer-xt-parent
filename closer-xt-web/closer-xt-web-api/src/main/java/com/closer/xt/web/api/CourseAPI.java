package com.closer.xt.web.api;

import com.closer.xt.common.annotation.NoAuth;
import com.closer.xt.common.cache.Cache;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.CourseParams;
import com.closer.xt.web.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course")
public class CourseAPI {
    @Autowired
    private CourseService courseService;

    @PostMapping("courseList")
    @NoAuth
    //此处缓存有些问题，需要：当订单完成支付，发一个消息到队列，队列接受到之后，把这里的缓存更新一下
    //@Cache(name = "web_courseList",time = 5*60*1000,hasUser = true)
    public CallResult courseList(@RequestBody CourseParams courseParams) {
        return courseService.courseList(courseParams);
    }

    @PostMapping("subjectInfo")
    public CallResult subjectInfo(@RequestBody CourseParams courseParams) {
        return courseService.subjectInfo(courseParams);
    }

    @PostMapping("courseDetail")
    public CallResult courseDetail(@RequestBody CourseParams courseParams) {
        return courseService.courseDetail(courseParams);
    }

    @PostMapping("myCoupon")
    public CallResult myCoupon(@RequestBody CourseParams courseParams) {
        return courseService.myCoupon(courseParams);
    }
}
