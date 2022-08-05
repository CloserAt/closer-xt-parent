package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.domain.CourseDomain;
import com.closer.xt.admin.domain.repository.CourseDomainRepository;
import com.closer.xt.admin.params.CourseParams;
import com.closer.xt.admin.service.CourseService;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseServiceImpl extends AbstractService implements CourseService {
    @Autowired
    private CourseDomainRepository courseDomainRepository;

    @Override
    public CallResult findPage(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.findPage(courseParams);
            }
        });
    }

    @Override
    public CallResult findCourseById(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.findCourseById(courseParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult saveCourse(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.saveCourse(courseParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult updateCourse(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.updateCourse(courseParams);
            }
        });
    }
}
