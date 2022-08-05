package com.closer.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.dao.data.CourseMapper;
import com.closer.xt.admin.dao.data.CourseSubjectMapper;
import com.closer.xt.admin.domain.CourseDomain;
import com.closer.xt.admin.domain.SubjectDomain;
import com.closer.xt.admin.params.CourseParams;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.pojo.Course;
import com.closer.xt.pojo.CourseSubject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CourseDomainRepository {
    @Resource
    private CourseMapper courseMapper;

    @Autowired
    private CourseSubjectMapper courseSubjectMapper;


    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    public CourseDomain createDomain(CourseParams courseParams) {
        return new CourseDomain(this, courseParams);
    }

    public Page<Course> findCourseListPage(int currentPage, int pageSize) {
        Page<Course> coursePage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotBlank(queryString)) {
//            queryWrapper.eq(Course::getCourseName,queryString);
//        }
        return this.courseMapper.selectPage(coursePage,queryWrapper);
    }

    public Course findCourseById(CourseParams courseParams) {
        Long courseId = courseParams.getId();
        return courseMapper.selectById(courseId);
    }

    public SubjectDomain createSubjectDomain(SubjectParams subjectParams) {
        return subjectDomainRepository.createDomain(subjectParams);
    }

    public void saveCourse(Course course) {
        this.courseMapper.insert(course);
    }

    public void saveCourseSubject(Long subjectId, Long courseId) {
        CourseSubject courseSubject = new CourseSubject();
        courseSubject.setCourseId(courseId);
        courseSubject.setSubjectId(subjectId);
        this.courseSubjectMapper.insert(courseSubject);
    }

    public void updateCourse(Course course) {
        this.courseMapper.updateById(course);
    }

    public void deleteCourseSubject(Long id) {
        LambdaQueryWrapper<CourseSubject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseSubject::getCourseId, id);
        this.courseSubjectMapper.delete(queryWrapper);
    }
}
