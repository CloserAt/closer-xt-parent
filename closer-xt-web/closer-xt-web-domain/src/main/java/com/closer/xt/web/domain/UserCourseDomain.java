package com.closer.xt.web.domain;

import com.closer.xt.pojo.UserCourse;
import com.closer.xt.web.domain.repository.UserCourseDomainRepository;
import com.closer.xt.web.model.params.UserCourseParams;

public class UserCourseDomain {
    private UserCourseDomainRepository userCourseDomainRepository;
    private UserCourseParams userCourseParams;
    public UserCourseDomain(UserCourseDomainRepository userCourseDomainRepository, UserCourseParams userCourseParams) {
        this.userCourseDomainRepository = userCourseDomainRepository;
        this.userCourseParams = userCourseParams;
    }

    public long countUserCourseByCourseId(Long id) {
        return this.userCourseDomainRepository.countUserCourseByCourseId(id);
    }

    public UserCourse findUserCourse(Long userId, Long courseId, long currentTimeMillis) {
        return this.userCourseDomainRepository.findUserCourse(userId,courseId,currentTimeMillis);
    }
}
