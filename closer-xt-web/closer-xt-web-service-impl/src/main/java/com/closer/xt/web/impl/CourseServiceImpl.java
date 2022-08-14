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
                return courseDomain.courseList();
            }
        });
    }

    @Override
    public CallResult subjectInfo(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkParam() {
                return courseDomain.checkSubjectInfoParam();
            }

            @Override
            public CallResult<Object> doAction() {
                return courseDomain.subjectInfo();
            }
        });
    }

    @Override
    public CallResult courseDetail(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.courseDetail(courseParams);
            }
        });
    }

    @Override
    public CallResult myCoupon(CourseParams courseParams) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.myCoupon(courseParams);
            }
        });
    }

    @Override
    public CallResult myCourse() {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(new CourseParams());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.myCourse();
            }
        });
    }
}
