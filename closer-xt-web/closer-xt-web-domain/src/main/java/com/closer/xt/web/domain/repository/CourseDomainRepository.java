package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.pojo.Course;
import com.closer.xt.pojo.CourseSubject;
import com.closer.xt.web.dao.CourseMapper;
import com.closer.xt.web.dao.CourseSubjectMapper;
import com.closer.xt.web.domain.*;
import com.closer.xt.web.model.params.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseDomainRepository {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSubjectMapper courseSubjectMapper;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Autowired
    private UserHistoryDomainRepository userHistoryDomainRepository;

    @Autowired
    private CouponDomainRepository couponDomainRepository;


    public CourseDomain createDomain(CourseParams courseParams) {
        return new CourseDomain(this,courseParams);
    }

    public Page<Course> findCourseByGrade(int currentPage, int pageSize, String subjectGrade) {
        Page<Course> page = new Page<>(currentPage,pageSize);
        /**
         * 三表联合查询：根绝subjectGrade从t_subject表中找到对应subject_id
         * 再根据subject_id在t_course_subject中找到对应的course_id
         * 最后根据course_id在t_course表中找到所有的数据
         */
        return this.courseMapper.findCourseByGrade(page,subjectGrade);
    }

    public Page<Course> findAllCourse(int currentPage, int pageSize) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getCourseStatus,0);//在数据库中定义的0为正常状态，可以全部拿出
        Page<Course> page = new Page<>(currentPage,pageSize);
        return this.courseMapper.selectPage(page, queryWrapper);
    }

    public UserCourseDomain createUserCourseDomain(UserCourseParams userCourseParams) {
        return this.userCourseDomainRepository.createDomain(userCourseParams);
    }

    public SubjectDomain createSubjectDomain(SubjectParams subjectParams) {
        return this.subjectDomainRepository.createDomain(subjectParams);
    }

    public Course findCourseById(Long courseId) {
        return this.courseMapper.selectById(courseId);
    }

    public UserHistoryDomain createUserHistoryDomain(UserHistoryParams userHistoryParams) {
        return this.userHistoryDomainRepository.createDomain(userHistoryParams);
    }

    public List<Long> findCourseIdListBySubject(Long subjectId) {
        LambdaQueryWrapper<CourseSubject> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CourseSubject::getSubjectId,subjectId);
        List<CourseSubject> list = this.courseSubjectMapper.selectList(queryWrapper);
        return list.stream().map(CourseSubject::getCourseId).collect(Collectors.toList());
    }

    public CouponDomain createCouponDomain(CouponParams couponParams) {
        return this.couponDomainRepository.creatDomain(couponParams);
    }

}
