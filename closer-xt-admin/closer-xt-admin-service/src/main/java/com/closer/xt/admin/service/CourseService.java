package com.closer.xt.admin.service;

import com.closer.xt.admin.params.CourseParams;
import com.closer.xt.common.model.CallResult;

public interface CourseService {
    /**
     * 更新课程
     * @param courseParams
     * @return
     */
    CallResult updateCourse(CourseParams courseParams);

    /**
     * 分页查询
     * @param courseParams
     * @return
     */
    CallResult findPage(CourseParams courseParams);

    /**
     * 根据id找course
     * @param courseParams
     * @return
     */
    CallResult findCourseById(CourseParams courseParams);

    /**
     * 保存
     * @param courseParams
     * @return
     */
    CallResult saveCourse(CourseParams courseParams);
}
