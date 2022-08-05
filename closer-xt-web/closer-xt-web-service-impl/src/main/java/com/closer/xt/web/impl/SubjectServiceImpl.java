package com.closer.xt.web.impl;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.web.domain.SubjectDomain;
import com.closer.xt.web.domain.repository.SubjectDomainRepository;
import com.closer.xt.web.model.params.SubjectParams;
import com.closer.xt.web.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl extends AbstractService implements SubjectService {
    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Override
    public CallResult listSubjectNew() {
        SubjectDomain subjectDomain = this.subjectDomainRepository.createDomain(new SubjectParams());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.listSubjectNew();
            }
        });
    }
}
