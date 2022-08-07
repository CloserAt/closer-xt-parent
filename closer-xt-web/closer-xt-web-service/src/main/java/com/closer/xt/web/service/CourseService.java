package com.closer.xt.web.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.CourseParams;

public interface CourseService {
    CallResult courseList(CourseParams courseParams);

    /**
     * 开始学习接口
     * @param courseParams
     * @return
     */
    CallResult subjectInfo(CourseParams courseParams);

    /**
     * 购买页面-课程详情
     * @param courseParams
     * @return
     */
    CallResult courseDetail(CourseParams courseParams);

    /**
     * 优惠券
     * @param courseParams
     * @return
     */
    CallResult myCoupon(CourseParams courseParams);
}
