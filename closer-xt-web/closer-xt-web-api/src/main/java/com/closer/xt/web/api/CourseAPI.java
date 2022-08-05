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
    @Cache(name = "web_courseList",time = 5*60*1000,hasUser = true)
    public CallResult courseList(@RequestBody CourseParams courseParams) {
        return CallResult.success(courseService.courseList(courseParams));
    }


}
