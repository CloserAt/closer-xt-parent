package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.pojo.Course;
import com.closer.xt.web.dao.CourseMapper;
import com.closer.xt.web.domain.CourseDomain;
import com.closer.xt.web.domain.SubjectDomain;
import com.closer.xt.web.domain.UserCourseDomain;
import com.closer.xt.web.model.params.CourseParams;
import com.closer.xt.web.model.params.SubjectParams;
import com.closer.xt.web.model.params.UserCourseParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseDomainRepository {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

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
}
