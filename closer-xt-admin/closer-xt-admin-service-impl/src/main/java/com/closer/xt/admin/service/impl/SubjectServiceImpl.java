package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.domain.SubjectDomain;
import com.closer.xt.admin.domain.repository.SubjectDomainRepository;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.admin.service.SubjectService;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl extends AbstractService implements SubjectService {
    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Override
    public CallResult findPage(SubjectParams subjectParams) {
        SubjectDomain subjectDomain = subjectDomainRepository.createDomain(subjectParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.findSubjectPage(subjectParams);
            }
        });
    }
}
