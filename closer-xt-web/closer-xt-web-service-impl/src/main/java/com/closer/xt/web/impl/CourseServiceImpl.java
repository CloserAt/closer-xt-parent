package com.closer.xt.web.impl;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.web.domain.CourseDomain;
import com.closer.xt.web.domain.repository.CourseDomainRepository;
import com.closer.xt.web.model.params.CourseParams;
import com.closer.xt.web.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends AbstractService implements CourseService {
    @Autowired
    private CourseDomainRepository courseDomainRepository;

    @Override
    public CallResult courseList(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.courseList(courseParams);
            }
        });
    }
}
