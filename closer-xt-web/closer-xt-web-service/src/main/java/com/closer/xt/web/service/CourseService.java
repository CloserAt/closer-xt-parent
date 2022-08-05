package com.closer.xt.web.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.CourseParams;

public interface CourseService {
    CallResult courseList(CourseParams courseParams);
}
