package com.closer.xt.web.domain;

import com.closer.xt.pojo.Course;
import com.closer.xt.pojo.Order;
import com.closer.xt.pojo.UserCourse;
import com.closer.xt.web.domain.repository.UserCourseDomainRepository;
import com.closer.xt.web.model.params.UserCourseParams;

import java.util.List;

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
        return userCourseDomainRepository.findUserCourse(userId,courseId,currentTimeMillis);
    }

    public Integer countUserCourseInCourseIdList(Long userId, List<Long> courseIdList, long currentTimeMillis) {
        return userCourseDomainRepository.countUserCourseInCourseIdList(userId,courseIdList,currentTimeMillis);
    }

    public void saveUserCourse(Order order) {
        Long courseId = order.getCourseId();
        Long userId = order.getUserId();
        UserCourse userCourse = this.userCourseDomainRepository.findUserCourseByUserIdAndCourseId(userId,courseId);
        if (userCourse == null) {
            userCourse = new UserCourse();
            userCourse.setCourseId(courseId);
            userCourse.setUserId(userId);
            userCourse.setCreateTime(System.currentTimeMillis());
            userCourse.setExpireTime(System.currentTimeMillis() + order.getExpireTime() * 24 * 60 * 60 * 1000L);
            userCourse.setStudyCount(0);
            this.userCourseDomainRepository.saveUserCourse(userCourse);
        } else {
            Long expireTime = userCourse.getExpireTime();
            Long currentTime = System.currentTimeMillis();
            //用户课程可能已经过期很久了,此处更新下
            if (currentTime >= expireTime) {
                expireTime = currentTime;
            }
            userCourse.setExpireTime(expireTime + order.getExpireTime() * 24 * 60 * 60 * 1000L);
            this.userCourseDomainRepository.updateUserCourse(userCourse);
        }
    }

    public List<UserCourse> findUserCourseList(Long userId) {
        return userCourseDomainRepository.findUserCourseList(userId);
    }
}
