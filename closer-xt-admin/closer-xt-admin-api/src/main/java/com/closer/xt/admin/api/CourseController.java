package com.closer.xt.admin.api;

import com.closer.xt.admin.params.CourseParams;
import com.closer.xt.admin.service.CourseService;
import com.closer.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("findPage")
    public CallResult findPage(@RequestBody CourseParams courseParams) {
        return courseService.findPage(courseParams);
    }

    @RequestMapping("findCourseById")
    public CallResult findCourseById(@RequestBody CourseParams courseParams) {
        return courseService.findCourseById(courseParams);
    }

    @RequestMapping("saveCourse")
    public CallResult saveCourse(@RequestBody CourseParams courseParams) {
        return courseService.saveCourse(courseParams);
    }

    @RequestMapping ("updateCourse")
    public CallResult updateCourse(@RequestBody CourseParams courseParams) {
        return courseService.updateCourse(courseParams);
    }
}
