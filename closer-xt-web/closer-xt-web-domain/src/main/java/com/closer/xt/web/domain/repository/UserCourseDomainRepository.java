package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.closer.xt.pojo.UserCourse;
import com.closer.xt.web.dao.UserCourseMapper;
import com.closer.xt.web.domain.UserCourseDomain;
import com.closer.xt.web.model.params.UserCourseParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCourseDomainRepository {
    @Autowired
    private UserCourseMapper userCourseMapper;

    public UserCourseDomain createDomain(UserCourseParams userCourseParams) {
        return new UserCourseDomain(this,userCourseParams);
    }

    public long countUserCourseByCourseId(Long id) {
        LambdaQueryWrapper<UserCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCourse::getCourseId,id);
        return this.userCourseMapper.selectCount(queryWrapper);
    }

    public UserCourse findUserCourse(Long userId, Long courseId, long currentTimeMillis) {
        LambdaQueryWrapper<UserCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCourse::getUserId,userId);
        queryWrapper.eq(UserCourse::getCourseId,courseId);
        queryWrapper.eq(UserCourse::getExpireTime,currentTimeMillis);
        return this.userCourseMapper.selectOne(queryWrapper);
    }
}
